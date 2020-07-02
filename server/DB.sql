
-- 졸업 필수학점 테이블:
create table credit(
    schoolyear int not null,            -- 입학년도
    college varchar(64) not null,       -- 단과대학
    department varchar(64) not null,    -- 학과명
    essential int not null,             -- 중핵필수
    choice int not null,                -- 중핵필수선택
    basicformajor int not null,         -- 전공기초교양
    major int not null,                 -- 전공학점계
    essentialmajor int not null,        -- 전공필수
    selectmajor int not null,           -- 전공선택
    graduation int not null,            -- 졸업학점
    primary key(schoolyear, department) -- 기본키(입학년도, 학과명)
);

-- 전공기초교양 과목 이수:
create table basicformajor (
    schoolyear int not null,                        -- 입학년도
    department varchar(64) not null,                -- 학과명
    class varchar(64) not null,                     -- 과목명
    credit int not null,                            -- 학점
    semester varchar(6) not null,                   -- 수강학기
    primary key(schoolyear, department, class)      -- 기본키(입학년도, 학과명, 과목명)
);

-- 중핵필수 과목 이수
create table essential (
    schoolyear int not null,                    -- 입학년도
    college varchar(64) not null,               -- 단과대학
    area varchar(32) not null,                  -- 영역
    class varchar(64) not null,                 -- 과목명
    credit int not null,                        -- 학점
    semester varchar(64) not null,              -- 수강학기
    primary key(schoolyear, college, class)     -- 기본키(입학년도, 단과대학, 과목명)
);

-- 중핵필수선택 과목 이수
create table choice (
    schoolyear int not null,            -- 입학년도
    college varchar(64) not null,       -- 단과대학
    condi varchar(256) not null,        -- 이수조건
    primary key(schoolyear, college)    -- 기본키(입학년도, 단과대학)
);

-- 단과별 중핵필수선택 필수 과목 
create table essentialcollege (
    schoolyear int not null,                        -- 입학년도
    college varchar(64) not null,                   -- 단과대학
    area varchar(32) not null,                      -- 영역
    class varchar(64) not null,                     -- 과목명
    credit int not null,                            -- 학점     
    semester varchar(32) not null,                  -- 수강학기
    primary key (schoolyear, college, class)        -- 기본키(입학년도, 단과대학, 과목명)
);

-- 학과별 중핵필수선택 필수 과목
create table essentialdepartment (
    schoolyear int not null,                      -- 입학년도
    department varchar(64) not null,              -- 학과명
    class varchar(64) not null,                   -- 과목명
    credit int not null,                          -- 학점     
    semester varchar(32) not null,                -- 수강학기
    primary key (schoolyear, department, class)   -- 기본키(입학년도, 단과대학, 과목명)
);

-- 학사일정
create table academicschedule (
    startyear int not null,                                 -- 시작 년도
    startmonth int not null,                                -- 시작 월
    startday int not null,                                  -- 시작 일
    startdate varchar(2) not null,                          -- 시작 요일
    endyear int not null,                                   -- 종료 년도
    endmonth int not null,                                  -- 종료 월 
    endday int not null,                                    -- 종료 일
    enddate varchar(2) not null,                            -- 종료 요일
    schedule varchar(258) not null,                         -- 스케쥴
    primary key (startyear, startmonth, startday, schedule) -- 기본키(시작 년도, 시작 월, 시작 일, 스케쥴)
);

-- 졸업영어
create table graduationeng (
    schoolyear int not null,        -- 입학 년도
    condi varchar(512) not null,    -- 졸업 조건
    primary key(schoolyear)         -- 기본키(입학 년도)
);

-- 졸업고전독서
create table graduationbook (
    schoolyear int not null,        -- 입학 년도
    eastern int not null,           -- 동양 철학
    western int not null,           -- 서양 철학
    easwestern int not null,        -- 동서양 철학
    science int not null,           -- 과학
    primary key(schoolyear)         -- 기본키(입학 년도)
);

-- 공학인증 요건
create table engineeringcertification (
    schoolyear int not null,            -- 입학 년도
    area varchar(64) not null,          -- 교과 영역
    mincredit varchar(64) not null,     -- 최소 이수학점
    essential varchar(512) not null,    -- 필수 사항
    primary key(schoolyear, area)       -- 기본키(입학 년도)
);
