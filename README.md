# CodeLineCounter
A small program to calculate the number of code line in a project.

## Feature

- File type filter
- Comment filter

## Compile

Just compile it as a normal java program. 

## Run

The command line form is shown as follow:

```
java CodeLineCounter [filterList] [isCommentIncluded]
```

*filterList*:

input those file types that you want to filter.

*isCommentIncluded*:

0 —> filter the comment.

1 —> don't filter the comment.

default is 1.

**Example:**

```
java CodeLineCounter html css js 0
```



