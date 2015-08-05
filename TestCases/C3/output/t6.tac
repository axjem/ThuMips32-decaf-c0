_global_addr: size 0

FUNCTION(_Binky) {
memo '_T1:4 _T2:8 _T3:12'
_Binky:
    _T0 = _global_addr
    _T4 = 0
    _T5 = 0
    _T6 = (_T4 < _T5)
    if (_T6 == 0) branch _L2
    _T7 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T7
    call _PrintString
    call _Halt
_L2:
    _T8 = 4
    _T9 = (_T4 * _T8)
    _T10 = (_T3 + _T9)
    _T11 = *(_T10 + 0)
    _T12 = 0
    _T13 = (_T11 < _T12)
    if (_T13 == 0) branch _L14
    _T14 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T14
    call _PrintString
    call _Halt
_L14:
    _T15 = 4
    _T16 = (_T11 * _T15)
    _T17 = (_T2 + _T16)
    _T18 = *(_T17 + 0)
    return _T18
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T21 = 4
    _T22 = 5
    _T23 = (_T21 * _T22)
    _T24 = 0
    _T25 = (_T23 < _T24)
    if (_T25 == 0) branch _L15
    _T26 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T26
    call _PrintString
    call _Halt
_L15:
    parm _T23
    _T27 =  call _Alloc
    _T20 = _T27
    _T28 = 0
    _T29 = 0
    _T30 = (_T28 < _T29)
    if (_T30 == 0) branch _L16
    _T31 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T31
    call _PrintString
    call _Halt
_L16:
    _T32 = 4
    _T33 = (_T28 * _T32)
    _T34 = (_T20 + _T33)
    _T35 = *(_T34 + 0)
    _T36 = 4
    _T37 = 12
    _T38 = (_T36 * _T37)
    _T39 = 0
    _T40 = (_T38 < _T39)
    if (_T40 == 0) branch _L17
    _T41 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T41
    call _PrintString
    call _Halt
_L17:
    parm _T38
    _T42 =  call _Alloc
    _T43 = 4
    _T44 = (_T28 * _T43)
    _T45 = (_T20 + _T44)
    *(_T45 + 0) = _T42
    _T46 = 4
    _T47 = 10
    _T48 = (_T46 * _T47)
    _T49 = 0
    _T50 = (_T48 < _T49)
    if (_T50 == 0) branch _L18
    _T51 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T51
    call _PrintString
    call _Halt
_L18:
    parm _T48
    _T52 =  call _Alloc
    _T19 = _T52
    _T53 = 0
    _T54 = 0
    _T55 = (_T53 < _T54)
    if (_T55 == 0) branch _L19
    _T56 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T56
    call _PrintString
    call _Halt
_L19:
    _T57 = 4
    _T58 = (_T53 * _T57)
    _T59 = (_T19 + _T58)
    _T60 = *(_T59 + 0)
    _T61 = 4
    _T62 = 5
    _T63 = 3
    _T64 = (_T62 * _T63)
    _T65 = 4
    _T66 = (_T64 / _T65)
    _T67 = 2
    _T68 = (_T66 % _T67)
    _T69 = (_T61 + _T68)
    _T70 = 4
    _T71 = (_T53 * _T70)
    _T72 = (_T19 + _T71)
    *(_T72 + 0) = _T69
    _T73 = 0
    _T74 = 0
    _T75 = (_T73 < _T74)
    if (_T75 == 0) branch _L20
    _T76 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T76
    call _PrintString
    call _Halt
_L20:
    _T77 = 4
    _T78 = (_T73 * _T77)
    _T79 = (_T20 + _T78)
    _T80 = *(_T79 + 0)
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
    _T87 = (_T19 + _T86)
    _T88 = *(_T87 + 0)
    _T89 = 0
    _T90 = (_T88 < _T89)
    if (_T90 == 0) branch _L22
    _T91 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T91
    call _PrintString
    call _Halt
_L22:
    _T92 = 4
    _T93 = (_T88 * _T92)
    _T94 = (_T80 + _T93)
    _T95 = *(_T94 + 0)
    _T96 = 55
    _T97 = 4
    _T98 = (_T88 * _T97)
    _T99 = (_T80 + _T98)
    *(_T99 + 0) = _T96
    _T100 = 0
    _T101 = 0
    _T102 = (_T100 < _T101)
    if (_T102 == 0) branch _L23
    _T103 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T103
    call _PrintString
    call _Halt
_L23:
    _T104 = 4
    _T105 = (_T100 * _T104)
    _T106 = (_T19 + _T105)
    _T107 = *(_T106 + 0)
    parm _T107
    call _PrintInt
    _T108 = " "
    parm _T108
    call _PrintString
    _T109 = 2
    _T110 = 100
    _T111 = 0
    _T112 = 0
    _T113 = (_T111 < _T112)
    if (_T113 == 0) branch _L24
    _T114 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T114
    call _PrintString
    call _Halt
_L24:
    _T115 = 4
    _T116 = (_T111 * _T115)
    _T117 = (_T20 + _T116)
    _T118 = *(_T117 + 0)
    parm _T110
    parm _T118
    parm _T19
    _T119 =  call _Binky
    _T120 = (_T109 * _T119)
    parm _T120
    call _PrintInt
}

