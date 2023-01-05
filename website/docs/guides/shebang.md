---
title: Shebang
sidebar_position: 30
---


This guide explains the differences between the `run` and `shebang` sub-commands, mainly covering how each of them
parses its arguments.

### `shebang` script headers

Before proceeding, let's discuss how `scala-cli` works in a script without the `shebang` command.
Here is a simple `hello.sc` script with a `shebang` header:

```scala title=hello.sc
#!/usr/bin/env scala-cli -S 3

println(args.size)
println(args.headOption)
```

And it works almost correctly.

<ChainedSnippets>

```bash 
chmod +x hello.sc
./hello.sc    
```

```text
0
None
```

<!-- Expected:
0
None
-->

</ChainedSnippets>

And it also works.

<ChainedSnippets>

```bash
./hello.sc -- Hello World
```

```text
2
Some(Hello)
```

<!-- Expected:
2
Some(Hello)
-->

</ChainedSnippets>

Note that the extra `--` must be added to make it work. If it is not supplied, the result is:

<ChainedSnippets>

```bash run-fail
./hello.sc Hello World
```

```text
[error] Hello: not found
World: not found
```

<!-- Expected:
Hello: not found
World: not found
-->

</ChainedSnippets>

If we modify our script slightly and use the `shebang` sub-command in the header, we will get the following:

```scala title=hello.sc
#!/usr/bin/env scala-cli shebang -S 3

println(args.size)
println(args.headOption)
```

<ChainedSnippets>

```bash
./hello.sc Hello World
```

```text
2
Some(Hello)
```
<!-- Expected:
2
Some(Hello)
-->

</ChainedSnippets>


### `shebang` and the command line

Let's now see how the `shebang` command works straight from the command line.

```scala title=Main.scala 
object Main {
  def main(args: Array[String]): Unit = println(args.mkString(" "))
}  
```

<ChainedSnippets>

```bash                                                                                                                                                                                                                                                                
scala-cli shebang Main.scala Hello world
```

```text
Hello world
```

<!-- Expected:
Hello world
-->

</ChainedSnippets>


:::note
Please note that `shebang` changing how arguments are parsed means that every option after the first input will be treated as
an argument to the app.

<ChainedSnippets>

```bash
scala-cli shebang Main.scala -S 2.13 #-S 2.13 is not recognised as an option, but as app arguments
```

```text
-S 2.13
```

<!-- Expected:
-S 2.13
-->

</ChainedSnippets>
:::

If we try to do the same with the `run` sub-command, we get the following error:

<ChainedSnippets>

```bash run-fail
scala-cli run Main.scala Hello world
```

```text
[error]  Hello: not found
world: not found
```

<!-- Expected:
[error]  Hello: not found
world: not found
-->

</ChainedSnippets>