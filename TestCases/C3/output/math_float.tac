_global_addr: size 0

FUNCTION(_abs) {
memo '_T1:4'
_abs:
    _T0 = _global_addr
    _T9 = 0
    _T10 = (_T1 >= _T9)
    if (_T10 == 0) branch _L6
    return _T1
    branch _L7
_L6:
    _T11 = 1
    _T12 = - _T1
    return _T12
_L7:
}

FUNCTION(_pow) {
memo '_T2:4 _T3:8'
_pow:
    _T0 = _global_addr
    _T15 = 1
    _T14 = _T15
    _T16 = 0
    _T13 = _T16
    branch _L8
_L9:
    _T17 = 1
    _T18 = (_T13 + _T17)
    _T13 = _T18
_L8:
    _T19 = (_T13 < _T3)
    if (_T19 == 0) branch _L10
    _T20 = (_T14 * _T2)
    _T14 = _T20
    branch _L9
_L10:
    return _T14
}

FUNCTION(_log) {
memo '_T4:4'
_log:
    _T0 = _global_addr
    _T21 = 1
    _T22 = (_T4 < _T21)
    if (_T22 == 0) branch _L11
    _T23 = 1
    _T24 = 1
    _T25 = - _T23
    move.f _F26 <= _T25
    cvt.f _F26 <= _F26
    return _F26
_L11:
    _T28 = 0
    _T27 = _T28
_L12:
    _T29 = 1
    _T30 = (_T4 > _T29)
    if (_T30 == 0) branch _L13
    _T31 = 1
    _T32 = 1
    _T33 = (_T27 + _T31)
    _T27 = _T33
    _T34 = 2
    _T35 = (_T4 / _T34)
    _T4 = _T35
    branch _L12
_L13:
    move.f _F36 <= _T27
    cvt.f _F36 <= _F36
    return _F36
}

FUNCTION(_max) {
memo '_F5:4 _T6:8'
_max:
    _T0 = _global_addr
    move.f _F37 <= _T6
    cvt.f _F37 <= _F37
    _T38 = (_F5 > _F37)
    if (_T38 == 0) branch _L14
    cvt.i _F39 <= _F5
    move.i _T40 <= _F39
    move.f _F41 <= _T40
    cvt.f _F41 <= _F41
    return _F41
    branch _L15
_L14:
    move.f _F42 <= _T6
    cvt.f _F42 <= _F42
    return _F42
_L15:
}

FUNCTION(_min) {
memo '_F7:4 _T8:8'
_min:
    _T0 = _global_addr
    move.f _F43 <= _T8
    cvt.f _F43 <= _F43
    _T44 = (_F7 < _F43)
    if (_T44 == 0) branch _L16
    return _F7
    branch _L17
_L16:
    move.f _F45 <= _T8
    cvt.f _F45 <= _F45
    return _F45
_L17:
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T46 = 1
    _T47 = 1
    _T48 = - _T46
    parm _T48
    _T49 =  call _abs
    parm _T49
    call _PrintInt
    _T50 = "\n"
    parm _T50
    call _PrintString
    _T51 = 2
    _T52 = 3
    parm _T51
    parm _T52
    _T53 =  call _pow
    parm _T53
    call _PrintInt
    _T54 = "\n"
    parm _T54
    call _PrintString
    _T55 = 16
    parm _T55
    _F56 =  call _log
    parm _F56
    call _PrintFloat
    _T57 = "\n"
    parm _T57
    call _PrintString
    _T58 = 1
    _T59 = 2
    move.f _F60 <= _T58
    cvt.f _F60 <= _F60
    parm _F60
    parm _T59
    _F61 =  call _max
    parm _F61
    call _PrintFloat
    _T62 = "\n"
    parm _T62
    call _PrintString
    _T63 = 1
    _T64 = 2
    move.f _F65 <= _T63
    cvt.f _F65 <= _F65
    parm _F65
    parm _T64
    _F66 =  call _min
    parm _F66
    call _PrintFloat
    _T67 = "\n"
    parm _T67
    call _PrintString
}

