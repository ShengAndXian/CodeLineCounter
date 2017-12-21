# CodeLineCounter
A small program to calculate the number of code line in a project.

<br/>

#### Feature

- File type filter
- Comment filter

<br/>

#### Compile

Just compile it as a normal java program. 

<br/>

#### Run

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

<br/>

Example:

```
java CodeLineCounter html css js 0
```



