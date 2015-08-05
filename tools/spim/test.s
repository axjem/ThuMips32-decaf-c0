.data
.align 2
why: .space 2

.text
main:
	li $t0, 0x05040302
	mtc1 $t0, $f0
	cvt.s.w $f0, $f0
	la $t2, glo0
	sw $t0, 0($t2)
	lb $t1, 2($t2)
	s.s $f0, 8($sp)
	l.s $f2, 8($sp)
	mov.s $f4, $f0
	mfc1 $v0, $f0
	mtc1 $v0, $f8
#	sb $t0, 12($sp)
#	lb $t1, 12($sp)
#	li $v0, 6
#	syscall
#	s.s $f0, 4($sp)
#	jal _PrintFloat

	li.s $f6, 0.2
	mfc1 $t4, $f6
	beqz $t4, here
	li $t6, 9
here:
	li $t6, 5
	jr $ra
	
.data
.align 2
glo0: .space 9