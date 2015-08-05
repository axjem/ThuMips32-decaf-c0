_global_addr: size 4

FUNCTION(_create) {
memo '_T1:4'
_create:
    _T0 = _global_addr
    _T2 = *(_T0 + 0)
    _T3 = 4
    _T4 = (_T3 * _T1)
    _T5 = 0
    _T6 = (_T4 < _T5)
    if (_T6 == 0) branch _L2
    _T7 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T7
    call _PrintString
    call _Halt
_L2:
    parm _T4
    _T8 =  call _Alloc
    *(_T0 + 0) = _T8
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T9 = 1
    _T10 = 1
    _T11 = - _T9
    parm _T11
    call _create
    _T12 = "after"
    parm _T12
    call _PrintString
}

