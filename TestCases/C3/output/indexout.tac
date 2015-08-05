_global_addr: size 0

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T2 = 4
    _T3 = 2
    _T4 = (_T2 * _T3)
    _T5 = 0
    _T6 = (_T4 < _T5)
    if (_T6 == 0) branch _L1
    _T7 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T7
    call _PrintString
    call _Halt
_L1:
    parm _T4
    _T8 =  call _Alloc
    _T1 = _T8
    _T9 = 2
    _T10 = 0
    _T11 = (_T9 < _T10)
    if (_T11 == 0) branch _L13
    _T12 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T12
    call _PrintString
    call _Halt
_L13:
    _T13 = 4
    _T14 = (_T9 * _T13)
    _T15 = (_T1 + _T14)
    _T16 = *(_T15 + 0)
    _T17 = 0
    _T18 = 4
    _T19 = (_T9 * _T18)
    _T20 = (_T1 + _T19)
    *(_T20 + 0) = _T17
    _T21 = "after:"
    parm _T21
    call _PrintString
    _T22 = 2
    _T23 = 0
    _T24 = (_T22 < _T23)
    if (_T24 == 0) branch _L14
    _T25 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T25
    call _PrintString
    call _Halt
_L14:
    _T26 = 4
    _T27 = (_T22 * _T26)
    _T28 = (_T1 + _T27)
    _T29 = *(_T28 + 0)
    parm _T29
    call _PrintInt
}

