# toexp

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

- toexp-idempotent
- toexp-enhanced-openfeign

## toexp-idempotent

Provides an aop way to make api method idempotent.
The default implementation `toexp-idempotent-redisson` is based on redisson,
but you can implement your own idempotent strategy by reimplementing the interfaces `IIdempotentContextProvider` and
`IIdempotentProvider`.

## toexp-enhanced-openfeign

Provides a workaround for openfeign to support `@RequestMapping` on interfaces.
Let rpc calls work like the `dubbo` way, the api provider declare the api interface in a seperate api package, 
and the consumer can use the api interface directly by importing it in the maven pom. 
