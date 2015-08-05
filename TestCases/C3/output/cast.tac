_global_addr: size 0

FUNCTION(_strcpy) {
memo '_T1:4 _T2:8'
_strcpy:
    _T0 = _global_addr
    _T4 = 0
    _T3 = _T4
_L2:
    _T5 = 0
    _T6 = (_T3 < _T5)
    if (_T6 == 0) branch _L3
    _T7 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T7
    call _PrintString
    call _Halt
_L3:
    _T8 = 1
    _T9 = (_T3 * _T8)
    _T10 = (_T2 + _T9)
    _T11 =.b *(_T10 + 0)
    _T12 = 0
    _T13 = (_T11 != _T12)
    if (_T13 == 0) branch _L15
    _T14 = 0
    _T15 = (_T3 < _T14)
    if (_T15 == 0) branch _L16
    _T16 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T16
    call _PrintString
    call _Halt
_L16:
    _T17 = 1
    _T18 = (_T3 * _T17)
    _T19 = (_T1 + _T18)
    _T20 =.b *(_T19 + 0)
    _T21 = 0
    _T22 = (_T3 < _T21)
    if (_T22 == 0) branch _L17
    _T23 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T23
    call _PrintString
    call _Halt
_L17:
    _T24 = 1
    _T25 = (_T3 * _T24)
    _T26 = (_T2 + _T25)
    _T27 =.b *(_T26 + 0)
    _T28 = 1
    _T29 = (_T3 * _T28)
    _T30 = (_T1 + _T29)
    *(_T30 + 0) =.b _T27
    _T31 = 1
    _T32 = (_T3 + _T31)
    _T3 = _T32
    branch _L2
_L15:
    _T33 = 0
    _T34 = (_T3 < _T33)
    if (_T34 == 0) branch _L18
    _T35 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T35
    call _PrintString
    call _Halt
_L18:
    _T36 = 1
    _T37 = (_T3 * _T36)
    _T38 = (_T1 + _T37)
    _T39 =.b *(_T38 + 0)
    _T40 = 0
    _T41 = 1
    _T42 = (_T3 * _T41)
    _T43 = (_T1 + _T42)
    *(_T43 + 0) =.b _T40
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T45 = 77
    _T44 = _T45
    _T46 = _T44
    _T47 = _T46
    _T48 = _T47
    parm _T44
    call _PrintInt
    _T49 = 32
    parm _T49
    call _PrintChar
    parm _T46
    call _PrintChar
    _T50 = 32
    parm _T50
    call _PrintChar
    parm _T47
    call _PrintBool
    _T51 = 32
    parm _T51
    call _PrintChar
    parm _T48
    call _PrintInt
    _T52 = 10
    parm _T52
    call _PrintChar
    move.f _F54 <= _T44
    cvt.f _F54 <= _F54
    _F53 = _F54
    move.f _F56 <= _T46
    cvt.f _F56 <= _F56
    _F55 = _F56
    move.f _F58 <= _T47
    cvt.f _F58 <= _F58
    _F57 = _F58
    _T60 = 10
    _T61 = 1
    _T62 = (_T61 * _T60)
    _T63 = 0
    _T64 = (_T62 < _T63)
    if (_T64 == 0) branch _L19
    _T65 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T65
    call _PrintString
    call _Halt
_L19:
    parm _T62
    _T66 =  call _Alloc
    _T59 = _T66
    _T67 = "aaaa"
    parm _T59
    parm _T67
    call _strcpy
    _T68 = _T59
    _T69 = 3
    _T70 = 0
    _T71 = (_T69 < _T70)
    if (_T71 == 0) branch _L20
    _T72 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T72
    call _PrintString
    call _Halt
_L20:
    _T73 = 1
    _T74 = (_T69 * _T73)
    _T75 = (_T59 + _T74)
    _T76 =.b *(_T75 + 0)
    parm _T76
    call _PrintChar
    _T77 = 32
    parm _T77
    call _PrintChar
    parm _F53
    call _PrintFloat
    _T78 = 32
    parm _T78
    call _PrintChar
    parm _F55
    call _PrintFloat
    _T79 = 32
    parm _T79
    call _PrintChar
    parm _F57
    call _PrintFloat
    _T80 = 32
    parm _T80
    call _PrintChar
    _T81 = 0
    _T82 = 0
    _T83 = (_T81 < _T82)
    if (_T83 == 0) branch _L21
    _T84 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T84
    call _PrintString
    call _Halt
_L21:
    _T85 = 4
    _T86 = (_T81 * _T85)
    _T87 = (_T68 + _T86)
    _T88 = *(_T87 + 0)
    parm _T88
    call _PrintInt
    _T89 = 10
    parm _T89
    call _PrintChar
    _T90 = 0
    _T91 = 0
    _T92 = (_T90 < _T91)
    if (_T92 == 0) branch _L22
    _T93 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T93
    call _PrintString
    call _Halt
_L22:
    _T94 = 4
    _T95 = (_T90 * _T94)
    _T96 = (_T68 + _T95)
    _T97 = *(_T96 + 0)
    _T98 = 58621
    _T99 = (_T97 + _T98)
    parm _T99
    call _PrintInt
    _T100 = 32
    parm _T100
    call _PrintChar
    _T101 = (_T47 + _T44)
    parm _T101
    call _PrintInt
    _T102 = 32
    parm _T102
    call _PrintChar
    cvt.i _F103 <= _F53
    move.i _T104 <= _F103
    parm _T104
    call _PrintInt
    _T105 = 10
    parm _T105
    call _PrintChar
}

