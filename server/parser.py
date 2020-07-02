import pandas as pd
import requests
from bs4 import BeautifulSoup
import pymysql
from itertools import product

def table_to_2d(table_tag):
    rowspans = [] 
    rows = table_tag.find_all('tr')

    colcount = 0
    for r, row in enumerate(rows):
        cells = row.find_all(['td', 'th'], recursive=False)
        colcount = max(
            colcount,
            sum(int(c.get('colspan', 1)) or 1 for c in cells[:-1]) + len(cells[-1:]) + len(rowspans))
        rowspans += [int(c.get('rowspan', 1)) or len(rows) - r for c in cells]
        rowspans = [s - 1 for s in rowspans if s > 1]

    table = [[None] * colcount for row in rows]

    rowspans = {}  
    for row, row_elem in enumerate(rows):
        span_offset = 0  
        for col, cell in enumerate(row_elem.find_all(['td', 'th'], recursive=False)):
            col += span_offset
            while rowspans.get(col, 0):
                span_offset += 1
                col += 1

            rowspan = rowspans[col] = int(cell.get('rowspan', 1)) or len(rows) - row
            colspan = int(cell.get('colspan', 1)) or colcount - col
            span_offset += colspan - 1
            value = cell.get_text()
            for drow, dcol in product(range(rowspan), range(colspan)):
                try:
                    table[row + drow][col + dcol] = value
                    rowspans[col + dcol] = rowspan
                except IndexError:
                    pass

        rowspans = {c: s - 1 for c, s in rowspans.items() if s > 1}

    return table

def dbConnect():
    DBUSER = 'hackathon'
    DBPW = 'hack2020'
    HOST = 'jonathan97son.iptime.org'
    DB = 'hackathon'

    parse_db = pymysql.connect(
        user = DBUSER,
        passwd = DBPW,
        host = HOST,
        db = DB,
        charset = 'utf8'
    )
    print(u'ParseDB is connected')
    return parse_db

def dbClose(parse_db, cursor):
    cursor.close()
    parse_db.close()
    print(u'ParseDB is closed')

def get_html(URL):
    _html = ""
    response = requests.get(URL)
    if response.status_code == 200:
        _html = response.text
    return _html

def cleanTables(cursor, parse_db):
    sqls = ['DELETE FROM CREDIT', 'DELETE FROM BASICFORMAJOR', 'DELETE FROM ESSENTIAL', 'DELETE FROM CHOICE', 'DELETE FROM ESSENTIALCOLLEGE', 'DELETE FROM ESSENTIALDEPARTMENT', 'DELETE FROM ACADEMICSCHEDULE', 'DELETE FROM GRADUATIONENG', 'DELETE FROM GRADUATIONBOOK', 'DELETE FROM ENGINEERINGCERTIFICATION']
    for sql in sqls:
        cursor.execute(sql)
    parse_db.commit()

def get_course_data(start_year, cnt, cursor, parse_db):
    for year in range(start_year, start_year + cnt):
        url = 'http://www.sejong.ac.kr/unilife/subject_' + str(year) + '.html'
        html = get_html(url)

        soup = BeautifulSoup(html, 'html.parser')

        divs = soup.find_all("div", {"class": "table_wrap"})
        
        count = 0
        length = len(divs)
        for div in divs:
            if count == 0:
                get_graduation_table(div.find("table"), cursor, parse_db, year)
            if count == 1:
                get_essential_table(div.find("table"), cursor, parse_db, year)
            if count == 2:
                get_choiceCondition_table(div.find("table"), cursor, parse_db, year)
            if count == 3:
                get_essentialCollege_table(div.find("table"), cursor, parse_db, year)
            if (year == 2015 and count == 4) or ((year == 2016 or year == 2017) and count == 6):
                get_essentialDepartment(div.find("table"), cursor, parse_db, year)
            if count == (length - 2) or count == (length - 3):
                get_basicformajor_table(div.find("table"), cursor, parse_db, year)
            if count == (length - 1):
                get_credit_table(div.find("table"), cursor, parse_db, year)
            count += 1

def get_graduation_table(table, cursor, parse_db, year):
    tables = table_to_2d(table)
    eng = tables[len(tables) - 2]
    eng[3] = eng[3].replace('\t', '')
    eng[3] = eng[3].replace('\n', '')
    eng[3] = eng[3].replace('  ', '')
    eng[3] = eng[3].replace('※', '\n※')
    data = []
    data.append(year)
    data.append(eng[3])

    sql = 'INSERT INTO graduationeng (schoolyear, condi) values (%s, %s);'
    cursor.execute(sql, data)
    parse_db.commit()

    data = []
    data.append(year)
    data.append(4)
    data.append(2)
    data.append(3)
    data.append(1)

    sql = 'INSERT INTO graduationbook (schoolyear, eastern, western, easwestern, science) values (%s, %s, %s, %s, %s);'
    cursor.execute(sql, data)
    parse_db.commit()

def get_essential_table(table, cursor, parse_db, year):
    tables = table_to_2d(table)
    head = tables[0]
    length = len(head)
    del tables[0]
    del tables[len(tables) - 1]
    for i in range(0, length):
        head[i] = head[i].replace('\t', '')
    
    cnt = 0
    for table in tables:
        if table[0] == None:
            del tables[cnt]
        cnt += 1
    
    datas = []
    for table in tables:
        length = len(table)
        area = table[0]
        classname = table[1]
        credit = table[2]
        for i in range(3, length):
            data = []
            data.append(year)
            data.append(head[i])
            data.append(area)
            data.append(classname)
            data.append(credit)
            data.append(table[i])
            datas.append(data)
    sql = 'INSERT INTO essential (schoolyear, college, area, class, credit, semester) values (%s, %s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_choiceCondition_table(table, cursor, parse_db, year):
    tables = table_to_2d(table)
    head = tables[0]
    body = tables[1]

    datas = []
    cnt = 0
    for con in body:
        data = []
        data.append(year)
        data.append(head[cnt])
        condition = '사상과역사, 사회와문화, 융합과창업, 자연과과학기술, 세계와지구촌 중 ' + con
        data.append(condition)
        cnt += 1
        datas.append(data)
    
    sql = 'INSERT INTO choice (schoolyear, college, condi) values (%s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_essentialCollege_table(table, cursor, parse_db, year):
    tables = table_to_2d(table)
    head = tables[0]
    del tables[0]
    del tables[len(tables) - 1]
    
    cnt = 0
    datas = []
    for table in tables:
        length = len(table)
        area = table[0]
        classname = table[1]
        credit = table[2]
        for i in range(3, length):
            if table[i] == '-' or table[i] == '\xa0':
                continue
            elif table[i] == '아래참조':
                table[i] = '1-1 또는 1-2'
            elif table[i] == "2" or table[i] == " 2":
                table[i] = '2학년'
            data = []
            data.append(year)
            data.append(head[i])
            data.append(area)
            data.append(classname)
            data.append(credit)
            data.append(table[i])
            datas.append(data)
            
    sql = 'INSERT INTO essentialcollege (schoolyear, college, area, class, credit, semester) values (%s, %s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_essentialDepartment(table, cursor, parse_db, year):
    tables = table_to_2d(table)
    head = tables[0]
    del tables[0]
    del tables[len(tables) - 1]
    
    cnt = 0
    datas = []
    for table in tables:
        length = len(table)
        classname = table[0]
        credit = table[1]
        for i in range(2, length):
            if table[i] == '지정해제' or table[i] == '\xa0':
                continue
            data = []
            data.append(year)
            data.append(head[i])
            data.append(classname)
            data.append(credit)
            data.append(table[i])
            datas.append(data)
            
    sql = 'INSERT INTO essentialdepartment (schoolyear, department, class, credit, semester) values (%s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_basicformajor_table(table, cursor, parse_db, year):
    thead = table.find("thead")
    tr = thead.find("tr")
    ths = tr.find_all("th")
    head = []
    for th in ths:
        head.append(th.text)

    tbody = table.find("tbody")
    trs = tbody.find_all("tr")
    datas = []
    for tr in trs:
        classname = ""
        credit = 0
        tds = tr.find_all("td")
        count = 0
        for td in tds:
            data = []
            if count == 0:
                classname = td.text
            elif count == 1:
                if td.text == "2" or td.text == "3":
                    credit = int(td.text)
                else:
                    credit = 3
            else:
                text = td.text
                if (text == '1-1' or text == '1-2' or text =='2-1' or text == '2-2' or text == '(A)' or text == '(B)' or text == '(A)(B)'):
                    data.append(year)
                    data.append(head[count])
                    data.append(classname)
                    data.append(credit)
                    data.append(text)
                    datas.append(data)
                else:
                    continue
            count += 1
        
    sql = 'INSERT INTO basicformajor (schoolyear, department, class, credit, semester) values (%s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_credit_table(table, cursor, parse_db, year):
    tbody = table.find("tbody")
    trs = tbody.find_all("tr")
    college = ""
    datas = []
    cnt = 0
    leng = len(trs)
    for tr in trs:
        data = []
        data.append(year)
        tds = tr.find_all("td")
        length = len(tds)
        count = 0
        for td in tds:
            if cnt == leng - 1:
                college = "-"
            if length == 9 and count == 0:
                college = td.text
                length = 8
                continue
            elif count == 0:
                data.append(college)
                data.append(td.text)
            else:
                data.append(int(td.text))
            count += 1
        datas.append(data)
        cnt += 1
    sql = 'INSERT INTO CREDIT (schoolyear, college, department, essential, choice, basicformajor, major, essentialmajor, selectmajor, graduation) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_schedule_data(cursor, parse_db):
    url = 'http://www.sejong.ac.kr/unilife/program_01.html'
    html = get_html(url)

    soup = BeautifulSoup(html, 'html.parser')
    divs = soup.find_all("div", {"class": "calendar_wrap"})

    count = 0
    length = len(divs)
    datas = []
    for div in divs:
        h4 = div.find("h4")
        yearmonth = h4.text.split('.')[0]
        year = yearmonth.split('년 ')[0]
        month = yearmonth.split(' ')[1]
        month = month.split('월')[0]
        spans = div.find_all("span")
        lis = div.find_all("li")
        cnt = 0
        for li in lis:
            data = []
            period = spans[cnt].text
            per = period.split('\xa0')
            period = ""
            for peri in per:
                period += peri
            content = li.text.split(spans[cnt].text)[1]
            content = content.strip()
            periods = period.split(' - ')
            length = len(periods)
            startyear = year
            startmonth = month
            startday = periods[0].split('(')[0]
            startdate = periods[0].split('(')[1].split(')')[0]
            data.append(startyear)
            data.append(startmonth)
            data.append(startday)
            data.append(startdate)
            
            endyear = startyear
            endmonth = startmonth
            endday = startday
            enddate = startdate
            if length == 2:
                endyear = startyear
                len1 = len(periods[1].split('.'))
                len2 = len(periods[1].split('/'))
                if len1 == 2:
                    endmonth = periods[1].split('.')[0]
                    daydate = periods[1].split('.')[1]
                elif len2 == 2:
                    endmonth = periods[1].split('/')[0]
                    daydate = periods[1].split('/')[1]
                else:
                    endmonth = startmonth
                    daydate = periods[1]
                endday = daydate.split('(')[0]
                enddate = daydate.split('(')[1].split(')')[0]
            data.append(endyear)
            data.append(endmonth)
            data.append(endday)
            data.append(enddate)
            data.append(content)
            datas.append(data)
            cnt += 1
    
    sql = 'INSERT INTO academicschedule (startyear, startmonth, startday, startdate, endyear, endmonth, endday, enddate, schedule) values (%s, %s, %s, %s, %s, %s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def get_engineeringcertification_data(year, cursor, parse_db):
    page = 2
    datas = []
    for i in range(0, 3):
        url = 'http://abeek.sejong.ac.kr/abeek/program0302_' + str(page + i) + '.html'
        r = requests.get(url)
        soup = BeautifulSoup(r.content.decode('euc-kr','replace'), 'html.parser')
        div = soup.find("div", {"class": "table0"})

        tables = table_to_2d(div.find("table"))
        del tables[0]
        for table in tables:
            table.insert(0, year + i)
            stri = table[1].split('\xa0')
            table[1] = ""
            for cnt in range(0, len(stri)):
                table[1] += stri[cnt]
            datas.append(table)
    
    sql = 'INSERT INTO engineeringcertification (schoolyear, area, mincredit, essential) values (%s, %s, %s, %s);'
    cursor.executemany(sql, datas)
    parse_db.commit()

def main():
    parse_db = dbConnect()
    cursor = parse_db.cursor()
    cleanTables(cursor, parse_db)
    get_schedule_data(cursor, parse_db)
    get_engineeringcertification_data(2015, cursor, parse_db)
    get_course_data(2015, 3, cursor, parse_db)
    dbClose(parse_db, cursor)

if __name__ == "__main__":
    main()