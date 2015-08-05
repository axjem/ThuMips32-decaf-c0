_global_addr: size 8

FUNCTION(_init) {
memo '_F1:4 _T2:8'
_init:
    _T0 = _global_addr
    _F3 = *(_T0 + 4)
    *(_T0 + 4) = _F1
    _F4 = *(_T0 + 0)
    move.f _F5 <= _T2
    cvt.f _F5 <= _F5
    *(_T0 + 0) = _F5
}

FUNCTION(_Moo) {
memo ''
_Moo:
    _T0 = _global_addr
    _F6 = *(_T0 + 0)
    parm _F6
    call _PrintFloat
    _T7 = " "
    parm _T7
    call _PrintString
    _F8 = *(_T0 + 4)
    parm _F8
    call _PrintFloat
    _T9 = "\n"
    parm _T9
    call _PrintString
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T10 = 100
    _T11 = 122
    move.f _F12 <= _T10
    cvt.f _F12 <= _F12
    parm _F12
    parm _T11
    call _init
    call _Moo
}

