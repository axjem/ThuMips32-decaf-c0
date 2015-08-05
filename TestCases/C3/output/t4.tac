_global_addr: size 8

FUNCTION(_tester) {
memo '_T1:4'
_tester:
    _T0 = _global_addr
    _T2 = *(_T0 + 4)
    _T3 = 4
    _T4 = 0
    _T5 = (_T3 < _T4)
    if (_T5 == 0) branch _L3
    _T6 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T6
    call _PrintString
    call _Halt
_L3:
    parm _T3
    _T7 =  call _Alloc
    *(_T0 + 4) = _T7
    _T8 = 4
    _T9 = (_T1 * _T8)
    _T10 = 0
    _T11 = (_T9 < _T10)
    if (_T11 == 0) branch _L15
    _T12 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T12
    call _PrintString
    call _Halt
_L15:
    parm _T9
    _T13 =  call _Alloc
    return _T13
}

FUNCTION(_start) {
memo ''
_start:
    _T0 = _global_addr
    _T17 = 1
    _T14 = _T17
_L16:
    _T18 = 5
    _T19 = (_T14 < _T18)
    if (_T19 == 0) branch _L17
    _T20 = 2
    _T21 = (_T14 % _T20)
    _T22 = 0
    _T23 = (_T21 == _T22)
    if (_T23 == 0) branch _L18
    parm _T14
    _T24 =  call _tester
    _T16 = _T24
    branch _L17
_L18:
    _T25 = "Loop "
    parm _T25
    call _PrintString
    parm _T14
    call _PrintInt
    _T26 = "\n"
    parm _T26
    call _PrintString
    _T27 = 1
    _T28 = (_T14 + _T27)
    _T14 = _T28
    branch _L16
_L17:
    _T29 = 0
    _T30 = 0
    _T31 = (_T29 < _T30)
    if (_T31 == 0) branch _L19
    _T32 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T32
    call _PrintString
    call _Halt
_L19:
    _T33 = 4
    _T34 = (_T29 * _T33)
    _T35 = (_T16 + _T34)
    _T36 = *(_T35 + 0)
    _T37 = 0
    _T38 = 4
    _T39 = (_T29 * _T38)
    _T40 = (_T16 + _T39)
    *(_T40 + 0) = _T37
    _T41 = 0
    _T42 = 0
    _T43 = (_T41 < _T42)
    if (_T43 == 0) branch _L20
    _T44 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T44
    call _PrintString
    call _Halt
_L20:
    _T45 = 4
    _T46 = (_T41 * _T45)
    _T47 = (_T16 + _T46)
    _T48 = *(_T47 + 0)
    _T49 = 0
    _T50 = (_T48 < _T49)
    if (_T50 == 0) branch _L21
    _T51 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T51
    call _PrintString
    call _Halt
_L21:
    _T52 = 4
    _T53 = (_T48 * _T52)
    _T54 = (_T16 + _T53)
    _T55 = *(_T54 + 0)
    parm _T55
    call _PrintInt
    _T56 = "\n"
    parm _T56
    call _PrintString
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    call _start
}

