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
    return _T25
_L11:
    _T27 = 0
    _T26 = _T27
_L12:
    _T28 = 1
    _T29 = (_T4 > _T28)
    if (_T29 == 0) branch _L13
    _T30 = 1
    _T31 = 1
    _T32 = (_T26 + _T30)
    _T26 = _T32
    _T33 = 2
    _T34 = (_T4 / _T33)
    _T4 = _T34
    branch _L12
_L13:
    return _T26
}

FUNCTION(_max) {
memo '_T5:4 _T6:8'
_max:
    _T0 = _global_addr
    _T35 = (_T5 > _T6)
    if (_T35 == 0) branch _L14
    return _T5
    branch _L15
_L14:
    return _T6
_L15:
}

FUNCTION(_min) {
memo '_T7:4 _T8:8'
_min:
    _T0 = _global_addr
    _T36 = (_T7 < _T8)
    if (_T36 == 0) branch _L16
    return _T7
    branch _L17
_L16:
    return _T8
_L17:
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T37 = 1
    _T38 = 1
    _T39 = - _T37
    parm _T39
    _T40 =  call _abs
    parm _T40
    call _PrintInt
    _T41 = "\n"
    parm _T41
    call _PrintString
    _T42 = 2
    _T43 = 3
    parm _T42
    parm _T43
    _T44 =  call _pow
    parm _T44
    call _PrintInt
    _T45 = "\n"
    parm _T45
    call _PrintString
    _T46 = 16
    parm _T46
    _T47 =  call _log
    parm _T47
    call _PrintInt
    _T48 = "\n"
    parm _T48
    call _PrintString
    _T49 = 1
    _T50 = 2
    parm _T49
    parm _T50
    _T51 =  call _max
    parm _T51
    call _PrintInt
    _T52 = "\n"
    parm _T52
    call _PrintString
    _T53 = 1
    _T54 = 2
    parm _T53
    parm _T54
    _T55 =  call _min
    parm _T55
    call _PrintInt
    _T56 = "\n"
    parm _T56
    call _PrintString
}

