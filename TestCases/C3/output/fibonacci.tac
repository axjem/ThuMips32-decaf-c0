_global_addr: size 0

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T3 = 0
    _T2 = _T3
_L2:
    _T4 = 10
    _T5 = (_T2 < _T4)
    if (_T5 == 0) branch _L3
    parm _T2
    _T6 =  call _get
    parm _T6
    call _PrintInt
    _T7 = "\n"
    parm _T7
    call _PrintString
    _T8 = 1
    _T9 = (_T2 + _T8)
    _T2 = _T9
    branch _L2
_L3:
}

FUNCTION(_get) {
memo '_T1:4'
_get:
    _T0 = _global_addr
    _T10 = 2
    _T11 = (_T1 < _T10)
    if (_T11 == 0) branch _L15
    _T12 = 1
    return _T12
_L15:
    _T13 = 1
    _T14 = (_T1 - _T13)
    parm _T14
    _T15 =  call _get
    _T16 = 2
    _T17 = (_T1 - _T16)
    parm _T17
    _T18 =  call _get
    _T19 = (_T15 + _T18)
    return _T19
}

