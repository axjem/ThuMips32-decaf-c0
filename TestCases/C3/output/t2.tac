_global_addr: size 4

FUNCTION(_strcpy) {
memo '_T1:4 _T2:8'
_strcpy:
    _T0 = _global_addr
    _T5 = 0
    _T4 = _T5
_L2:
    _T6 = 0
    _T7 = (_T4 < _T6)
    if (_T7 == 0) branch _L3
    _T8 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T8
    call _PrintString
    call _Halt
_L3:
    _T9 = 1
    _T10 = (_T4 * _T9)
    _T11 = (_T2 + _T10)
    _T12 =.b *(_T11 + 0)
    _T13 = 0
    _T14 = (_T12 != _T13)
    if (_T14 == 0) branch _L15
    _T15 = 0
    _T16 = (_T4 < _T15)
    if (_T16 == 0) branch _L16
    _T17 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T17
    call _PrintString
    call _Halt
_L16:
    _T18 = 1
    _T19 = (_T4 * _T18)
    _T20 = (_T1 + _T19)
    _T21 =.b *(_T20 + 0)
    _T22 = 0
    _T23 = (_T4 < _T22)
    if (_T23 == 0) branch _L17
    _T24 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T24
    call _PrintString
    call _Halt
_L17:
    _T25 = 1
    _T26 = (_T4 * _T25)
    _T27 = (_T2 + _T26)
    _T28 =.b *(_T27 + 0)
    _T29 = 1
    _T30 = (_T4 * _T29)
    _T31 = (_T1 + _T30)
    *(_T31 + 0) =.b _T28
    _T32 = 1
    _T33 = (_T4 + _T32)
    _T4 = _T33
    branch _L2
_L15:
    _T34 = 0
    _T35 = (_T4 < _T34)
    if (_T35 == 0) branch _L18
    _T36 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T36
    call _PrintString
    call _Halt
_L18:
    _T37 = 1
    _T38 = (_T4 * _T37)
    _T39 = (_T1 + _T38)
    _T40 =.b *(_T39 + 0)
    _T41 = 0
    _T42 = 1
    _T43 = (_T4 * _T42)
    _T44 = (_T1 + _T43)
    *(_T44 + 0) =.b _T41
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T3 = 3
    *(_T0 + 0) = _T3
    _T47 = "hello"
    _T46 = _T47
    _T49 = 5
    _T50 = 1
    _T51 = (_T49 * _T50)
    _T52 = 0
    _T53 = (_T51 < _T52)
    if (_T53 == 0) branch _L19
    _T54 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T54
    call _PrintString
    call _Halt
_L19:
    parm _T51
    _T55 =  call _Alloc
    _T46 = _T55
    _T56 = 1
    _T48 = _T56
    _T57 = "wow!"
    parm _T46
    parm _T57
    call _strcpy
    _T58 = 3
    _T45 = _T58
    if (_T48 == 0) branch _L20
    _T59 = *(_T0 + 0)
    _T60 = (_T45 * _T59)
    _T45 = _T60
_L20:
    parm _T48
    call _PrintBool
    _T61 = " "
    parm _T61
    call _PrintString
    parm _T45
    call _PrintInt
    _T62 = 32
    parm _T62
    call _PrintChar
    parm _T46
    call _PrintString
}

