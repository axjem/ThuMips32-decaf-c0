_global_addr: size 0

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T1 = 72
    parm _T1
    call _PrintChar
    _T2 = "hello world"
    parm _T2
    call _PrintString
    _T4 = 0
    _T3 = _T4
    _T5 = 0
    _T6 = (_T3 == _T5)
    if (_T6 == 0) branch _L12
    _F8 = 1.1
    _F7 = _F8
_L12:
    _T10 = 115
    _T9 = _T10
    _T11 = 115
    _T12 = (_T9 == _T11)
    if (_T12 == 0) branch _L14
    _T13 = "hello world"
    parm _T13
    call _PrintString
    branch _L15
_L14:
    _T14 = 100
    _T15 = (_T9 == _T14)
    if (_T15 == 0) branch _L16
_L15:
    _T16 = "hello"
    parm _T16
    call _PrintString
    branch _L13
    branch _L17
_L16:
    _T17 = 10
    _T18 = (_T9 == _T17)
    if (_T18 == 0) branch _L18
_L17:
    _T19 = "char to int"
    parm _T19
    call _PrintString
    branch _L19
_L18:
_L19:
    _T20 = "default"
    parm _T20
    call _PrintString
    branch _L13
_L20:
_L13:
}

