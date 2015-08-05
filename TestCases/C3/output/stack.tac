_global_addr: size 8

FUNCTION(_Init) {
memo ''
_Init:
    _T0 = _global_addr
    _T2 = *(_T0 + 4)
    _T3 = 100
    _T4 = 4
    _T5 = (_T3 * _T4)
    _T6 = 0
    _T7 = (_T5 < _T6)
    if (_T7 == 0) branch _L5
    _T8 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T8
    call _PrintString
    call _Halt
_L5:
    parm _T5
    _T9 =  call _Alloc
    *(_T0 + 4) = _T9
    _T10 = *(_T0 + 0)
    _T11 = 0
    *(_T0 + 0) = _T11
    _T12 = 3
    parm _T12
    call _Push
}

FUNCTION(_Push) {
memo '_T1:4'
_Push:
    _T0 = _global_addr
    _T13 = *(_T0 + 4)
    _T14 = *(_T0 + 0)
    _T15 = 4
    _T16 = (_T14 * _T15)
    _T17 = (_T13 + _T16)
    _T18 = *(_T17 + 0)
    _T19 = 4
    _T20 = 0
    _T21 = (_T20 * _T19)
    _T22 = (_T17 + _T21)
    *(_T22 + 0) = _T1
    _T23 = *(_T0 + 0)
    _T24 = 1
    _T25 = (_T23 + _T24)
    *(_T0 + 0) = _T25
}

FUNCTION(_Pop) {
memo ''
_Pop:
    _T0 = _global_addr
    _T27 = *(_T0 + 4)
    _T28 = *(_T0 + 0)
    _T29 = 1
    _T30 = (_T28 - _T29)
    _T31 = 0
    _T32 = (_T30 < _T31)
    if (_T32 == 0) branch _L17
    _T33 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T33
    call _PrintString
    call _Halt
_L17:
    _T34 = 4
    _T35 = (_T30 * _T34)
    _T36 = (_T27 + _T35)
    _T37 = *(_T36 + 0)
    _T26 = _T37
    _T38 = *(_T0 + 0)
    _T39 = 1
    _T40 = (_T38 - _T39)
    *(_T0 + 0) = _T40
    return _T26
}

FUNCTION(_NumElems) {
memo ''
_NumElems:
    _T0 = _global_addr
    _T41 = *(_T0 + 0)
    return _T41
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    call _Init
    _T42 = 3
    parm _T42
    call _Push
    _T43 = 7
    parm _T43
    call _Push
    _T44 = 4
    parm _T44
    call _Push
    _T45 =  call _NumElems
    parm _T45
    call _PrintInt
    _T46 = " "
    parm _T46
    call _PrintString
    _T47 =  call _Pop
    parm _T47
    call _PrintInt
    _T48 = " "
    parm _T48
    call _PrintString
    _T49 =  call _Pop
    parm _T49
    call _PrintInt
    _T50 = " "
    parm _T50
    call _PrintString
    _T51 =  call _Pop
    parm _T51
    call _PrintInt
    _T52 = " "
    parm _T52
    call _PrintString
    _T53 =  call _NumElems
    parm _T53
    call _PrintInt
}

