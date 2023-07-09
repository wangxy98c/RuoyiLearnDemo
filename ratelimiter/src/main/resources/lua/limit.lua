-- 传入的三个参数，下标从1开始
local key=KEYS[1]
local time=tonumber(ARGV[1])
local count=tonumber(ARGV[2])
local current=redis.call('get',key) --get方法，参数key表示要限流的那个接口。
if current and tonumber(current)>count then --current存在而且超过count了，即表示已经超过限流了
    return tonumber(current) --脚本返回
end
current=redis.call('incr',key) --未超过限流，自增。且不能用set，因为可能是并发操作，仅仅set无法区分
if tonumber(current)==1 then --自增后=1 说明没有其他人操作过此接口（设置过过期时间了)
    redis.call('expire',key,time) --设置过期时间
end
return tonumber(current) --java中判断返回的值来进行各种操作（限流/放行)
