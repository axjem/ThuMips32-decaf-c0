_global_addr: size 0

FUNCTION(_lengthOf) {
memo '_T1:4'
_lengthOf:
    _T0 = _global_addr
    _T7 = 0
    _T6 = _T7
_L4:
    _T8 = 0
    _T9 = (_T6 < _T8)
    if (_T9 == 0) branch _L5
    _T10 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T10
    call _PrintString
    call _Halt
_L5:
    _T11 = 1
    _T12 = (_T6 * _T11)
    _T13 = (_T1 + _T12)
    _T14 =.b *(_T13 + 0)
    _T15 = 0
    _T16 = (_T14 != _T15)
    if (_T16 == 0) branch _L17
    _T17 = 1
    _T18 = (_T6 + _T17)
    _T6 = _T18
    branch _L4
_L17:
    return _T6
}

FUNCTION(_strcmp) {
memo '_T2:4 _T3:8'
_strcmp:
    _T0 = _global_addr
    parm _T2
    _T19 =  call _lengthOf
    parm _T3
    _T20 =  call _lengthOf
    _T21 = (_T19 != _T20)
    if (_T21 == 0) branch _L18
    _T22 = 0
    return _T22
_L18:
    _T24 = 0
    _T23 = _T24
_L19:
    _T25 = 0
    _T26 = (_T23 < _T25)
    if (_T26 == 0) branch _L20
    _T27 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T27
    call _PrintString
    call _Halt
_L20:
    _T28 = 1
    _T29 = (_T23 * _T28)
    _T30 = (_T2 + _T29)
    _T31 =.b *(_T30 + 0)
    _T32 = 0
    _T33 = (_T31 != _T32)
    if (_T33 == 0) branch _L21
    _T34 = 0
    _T35 = (_T23 < _T34)
    if (_T35 == 0) branch _L22
    _T36 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T36
    call _PrintString
    call _Halt
_L22:
    _T37 = 1
    _T38 = (_T23 * _T37)
    _T39 = (_T2 + _T38)
    _T40 =.b *(_T39 + 0)
    _T41 = 0
    _T42 = (_T23 < _T41)
    if (_T42 == 0) branch _L23
    _T43 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T43
    call _PrintString
    call _Halt
_L23:
    _T44 = 1
    _T45 = (_T23 * _T44)
    _T46 = (_T3 + _T45)
    _T47 =.b *(_T46 + 0)
    _T48 = (_T40 != _T47)
    if (_T48 == 0) branch _L24
    _T49 = 0
    return _T49
_L24:
    _T50 = 1
    _T51 = (_T23 + _T50)
    _T23 = _T51
    branch _L19
_L21:
    _T52 = 1
    return _T52
}

FUNCTION(_printCompareString) {
memo '_T4:4 _T5:8'
_printCompareString:
    _T0 = _global_addr
    _T53 = "\""
    parm _T53
    call _PrintString
    parm _T4
    call _PrintString
    _T54 = "\" and \""
    parm _T54
    call _PrintString
    parm _T5
    call _PrintString
    _T55 = "\": "
    parm _T55
    call _PrintString
    parm _T4
    parm _T5
    _T56 =  call _strcmp
    parm _T56
    call _PrintBool
    _T57 = "\n"
    parm _T57
    call _PrintString
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T58 = "Jobs"
    _T59 = "Gates"
    parm _T58
    parm _T59
    call _printCompareString
    _T60 = "case sensitive"
    _T61 = "CASE SENSITIVE"
    parm _T60
    parm _T61
    call _printCompareString
    _T62 = "Hello"
    _T63 = "Hello"
    parm _T62
    parm _T63
    call _printCompareString
    _T66 =  call _ReadLine
    _T64 = _T66
    _T67 =  call _ReadLine
    _T65 = _T67
    parm _T64
    parm _T65
    call _printCompareString
}

