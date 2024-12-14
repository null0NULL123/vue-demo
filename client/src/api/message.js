import request from '@/utils/request.js'

export const messageListService = (data)=>{
    console.log(data);
    return request.post('/message/userlist', data)
}

export const messageSendService = (data)=>{
    return request.post('/message',data)
}

export const messageGetService = (data)=>{
    return request.post('/message/history',data)
}