Date: 05/02/2016
 
This program was written by Ricardo Sá Loureiro Ferreira da Silva,
a Computer Science student in the Faculty of Sciences, University of Oporto, Portugal.
This program was written in Java and its goal is
to implement a variant of the original Davis-Putnam algorithm from 1960 [DP60,DLL62].
I will try to briefly describe what each function does in the comments.
  
Feel free to use my code for projects and learning. If someone happens to do it I would
appreciate to be given credit.
  
Any questions ask away,
Thanks
Ricardo Ferreira da Silva

Regarding the input:

- Negation = "-"
- One Clause per line, where literals are separated by spaces.

Here is an example.

Formula: (-p v -q) ^ (-p v q) ^ (p v -q) ^ (p v q)

Formula in Normal Conjuctive Form: {[-p,-q],[-p,q],[p,-q],[p,q]}

Input:

4

-p -q

-p q

p -q

p q


