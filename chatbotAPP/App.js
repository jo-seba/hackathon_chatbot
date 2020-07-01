import React, { useState, useCallback, useEffect } from 'react';
import {Alert} from 'react-native'
import { GiftedChat, SystemMessage } from 'react-native-gifted-chat'

export default function App() {
  var idCount = 1;
  var convertText;
  const [messages, setMessages] = useState([]);
  
  useEffect(() => {
    setMessages([
      {
        _id: 1,
        text: '안녕하세요. 세종대학교 무엇이든물어봇입니다. 학번이 어떻게 되시나요?',
        createdAt: new Date(),
        user: {
          _id: 2,
          name: 'SejongBot',
          avatar: 'https://placeimg.com/140/140/any',
        },
      },
    ])
  }, [])

  const onSend = useCallback((messages = []) => {
    setMessages(previousMessages => GiftedChat.append(previousMessages, messages))
    convertText = JSON.stringify(messages,['text']);
    convertText = convertText.replace('[{"text":"','');
    convertText = convertText.replace('"}]','');
    alert(convertText);
    
    onReceive('응 나도 몰라')
  }, [])
  
  const onReceive = useCallback(text => {
    idCount = idCount + 1,
    setMessages(previousMessages => GiftedChat.append(previousMessages, [
      {
        _id: idCount,
        text: text,
        createdAt: new Date(),
        user: {
          _id: 2,
          name: 'SejongBot',
          avatar: 'https://placeimg.com/140/140/any',
        },
      },
    ]))
  }, [])

  return (
    <GiftedChat
      placeholder = '메세지를 입력하세요'
      messages={messages}
      onSend={messages => onSend(messages)}
    />
  );
}

