function login(resolve,reject){
    setTimeout(()=>{
        let number=Math.random();
        console.log('login===> ',number)
        if(number>0.5){
            //登陆成功，利用resolve函数把结果扔出去
            
            resolve("login success")
        }else{
            //登陆失败，用reject
            reject("login error")
        }
    },2000)
}
function getInfo(resolve,reject){
    setTimeout(()=>{
        let number=Math.random();
        console.log('getInfo===> ',number)
        if(number>0.5){
            //登陆成功，利用resolve函数把结果扔出去
            resolve("getInfo success")
        }else{
            //登陆失败，用reject
            reject("getInfo error")
        }
    },2000)
}
function getMenu(resolve,reject){
    setTimeout(()=>{
        let number=Math.random();
        console.log('getMenu===> ',number)
        if(number>0.5){
            //登陆成功，利用resolve函数把结果扔出去
            resolve("getMenu success")
        }else{
            //登陆失败，用reject
            reject("getMenu error")
        }
    },2000)
}
//连续形式
// new Promise(login).then(data=>{
//     //login成功时的回调（resolve时）
//     console.log('login:',data)
//     return new Promise(getInfo)
// }).then(data=>{
//     //getInfo成功时的回调
//     console.log('getInfo:',data)
//     return new Promise(getMenu)
// }).then(data=>{
//     //getMenu成功时的回调
//     console.log('getMenu:',data)
// }).catch(error=>{
//     //对于失败的情况不需要多个catch，任意一个Promise出异常都会进入最近的一个catch代码块中
//     //除非自己想分别处理，不然不需要多个。（进入最近的那个catch）
//     console.log("error:",error)
// })
/* 一个测试输出
login===>  0.6330050204165378
login: login success
getInfo===>  0.564759890647067
getInfo: getInfo success
getMenu===>  0.24244118320392372
error: getMenu error
*/

//then多参数
// new Promise(login).then(data=>{
//     console.log('login then:',data)
// },error=>{
//     console.log('error then:',error)
// })

//then返回字符串
// new Promise(login).then(data=>{
//     console.log('then1:',data)
//     return data+'1';
// }).then(data=>{
//     console.log('then2:',data)
//     return data+'2'
// }).then(data=>{
//     console.log('then3:',data)
//     return data+'3'
// }).catch(err=>{
//     console.log('error:',err)
//     return err+'4'
// })
//一个成功的测试输出
// login===>  0.759378843878912
// then1: login success
// then2: login success1
// then3: login success12
//一个失败的测试输出
// login===>  0.33134505946441895
// error: login error

//then抛异常
// new Promise(login).then(data=>{
//     console.log('then1:',data)
//     return data+'1';
// }).then(data=>{
//     console.log('then2:',data)
//     throw new Error('出错了2')
// }).then(data=>{
//     console.log('then3:',data)
// }).catch(err=>{
//     console.log('error:',err)
// }).finally(()=>{
//     console.log('执行finally')
// }).then(()=>{
//     console.log('finally 后面的then 代码')
// })
//一个测试
// login===>  0.5963416081378934
// then1: login success
// then2: login success1
// error: Error: 出错了2
//     at /Users/wangxy/code/RuoyiLearnDemo/SomethingTest/PromiseT.js:103:11
//     at process.processTicksAndRejections (node:internal/process/task_queues:95:5)
// 执行finally
// finally 后面的then 代码

// let p1=Promise.resolve('成功了！')//只会进入then
// p1.then(data=>{
//     console.log('data',data)
// }).catch(err=>{
//     //除非在then中抛出异常，否则不会进入
// })
// function resolved(){
//     console.log('resolved')
// }
// function rejected(err){
//     console.log('rejected:',err)
// }
// p1=Promise.reject('出错了!')//只会进入reject
// //p1.then(resolved,rejected) 或如下方式
// p1.then(resolved).catch(rejected)

// //let p1=Promise.resolve('resolve1')
// let p1=Promise.reject('reject!')
// //let p2=88
// let p2=Promise.reject('reject2')
// let p3=new Promise((resolve,reject)=>{
//     setTimeout(resolve,3000,"hello javaboy")
// })
// Promise.all([p1,p2,p3]).then(data=>{
//     //三个都是resolve，会进入then。数据是所有的返回组成的数组
//     console.log('data:',data)
//     //输出数据：   data: [ 'resolve1', 88, 'hello javaboy' ] 
// }).catch(err=>{
//     //只要有一个失败的情况，数据是第一个失败的数据
//     console.log('error:',err)
//     //输出数据：  error: reject!
// })

let p1=new Promise((resolve,reject)=>{
    setTimeout(resolve,100,"one")
})
let p2=new Promise((resolve,reject)=>{
    setTimeout(resolve,1000,"two")
})
Promise.race([p1,p2]).then(data=>{
    console.log('data:',data)
}).catch(err=>{
    console.log('err:',err)
})
//测试输出,输出非常迅速，不会等10s
// data: one