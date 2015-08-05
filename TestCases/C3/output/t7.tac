_global_addr: size 20

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T1 = 3
    *(_T0 + 0) = _T1
    _F2 = 2.3
    *(_T0 + 4) = _F2
    _T3 = 48
    *(_T0 + 8) =.b _T3
    _T4 = 111
    *(_T0 + 12) =.b _T4
    _T5 = 1
    *(_T0 + 16) = _T5
    _F7 = 0.1
    _F6 = _F7
    _F9 = 0.2
    _F8 = _F9
    _T11 = (_F6 > _F8)
    _T10 = _T11
    parm _T10
    call _PrintBool
    _T12 = 32
    parm _T12
    call _PrintChar
    _T13 =.b *(_T0 + 8)
    move.f _F14 <= _T13
    cvt.f _F14 <= _F14
    _F15 = (_F6 * _F14)
    parm _F15
    call _PrintFloat
    _T16 = " "
    parm _T16
    call _PrintString
    _T17 =.b *(_T0 + 12)
    move.f _F18 <= _T17
    cvt.f _F18 <= _F18
    _F19 = (_F18 / _F8)
    parm _F19
    call _PrintFloat
    _T20 = 32
    parm _T20
    call _PrintChar
    _T21 = *(_T0 + 16)
    move.f _F22 <= _T21
    cvt.f _F22 <= _F22
    _T23 = (_F8 != _F22)
    parm _T23
    call _PrintBool
    _T24 = 32
    parm _T24
    call _PrintChar
    cvt.i _F25 <= _F6
    move.i _T26 <= _F25
    parm _T26
    call _PrintInt
    _T27 = 10
    parm _T27
    call _PrintChar
}

