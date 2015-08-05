_global_addr: size 0

FUNCTION(_strcpy) {
memo '_T1:4 _T2:8'
_strcpy:
    _T0 = _global_addr
    _T6 = 0
    _T5 = _T6
_L3:
    _T7 = 0
    _T8 = (_T5 < _T7)
    if (_T8 == 0) branch _L4
    _T9 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T9
    call _PrintString
    call _Halt
_L4:
    _T10 = 1
    _T11 = (_T5 * _T10)
    _T12 = (_T2 + _T11)
    _T13 =.b *(_T12 + 0)
    _T14 = 0
    _T15 = (_T13 != _T14)
    if (_T15 == 0) branch _L16
    _T16 = 0
    _T17 = (_T5 < _T16)
    if (_T17 == 0) branch _L17
    _T18 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T18
    call _PrintString
    call _Halt
_L17:
    _T19 = 1
    _T20 = (_T5 * _T19)
    _T21 = (_T1 + _T20)
    _T22 =.b *(_T21 + 0)
    _T23 = 0
    _T24 = (_T5 < _T23)
    if (_T24 == 0) branch _L18
    _T25 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T25
    call _PrintString
    call _Halt
_L18:
    _T26 = 1
    _T27 = (_T5 * _T26)
    _T28 = (_T2 + _T27)
    _T29 =.b *(_T28 + 0)
    _T30 = 1
    _T31 = (_T5 * _T30)
    _T32 = (_T1 + _T31)
    *(_T32 + 0) =.b _T29
    _T33 = 1
    _T34 = (_T5 + _T33)
    _T5 = _T34
    branch _L3
_L16:
    _T35 = 0
    _T36 = (_T5 < _T35)
    if (_T36 == 0) branch _L19
    _T37 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T37
    call _PrintString
    call _Halt
_L19:
    _T38 = 1
    _T39 = (_T5 * _T38)
    _T40 = (_T1 + _T39)
    _T41 =.b *(_T40 + 0)
    _T42 = 0
    _T43 = 1
    _T44 = (_T5 * _T43)
    _T45 = (_T1 + _T44)
    *(_T45 + 0) =.b _T42
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T48 = 1
    _T49 = 10
    _T50 = (_T48 * _T49)
    _T51 = 0
    _T52 = (_T50 < _T51)
    if (_T52 == 0) branch _L20
    _T53 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T53
    call _PrintString
    call _Halt
_L20:
    parm _T50
    _T54 =  call _Alloc
    _T47 = _T54
    _T55 = "hello"
    parm _T47
    parm _T55
    call _strcpy
    _T56 = 4
    _T57 = 5
    move.f _F58 <= _T56
    cvt.f _F58 <= _F58
    parm _F58
    move.f _F59 <= _T57
    cvt.f _F59 <= _F59
    parm _F59
    _T60 =  call _test
    _T46 = _T60
    parm _T46
    call _PrintInt
    parm _T47
    call _PrintString
}

FUNCTION(_test) {
memo '_F3:4 _F4:8'
_test:
    _T0 = _global_addr
    _F61 = (_F3 + _F4)
    cvt.i _F62 <= _F61
    move.i _T63 <= _F62
    return _T63
}

