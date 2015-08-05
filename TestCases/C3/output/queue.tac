_global_addr: size 20

FUNCTION(_init_QueueItem) {
memo '_T1:4 _T2:8 _T3:12 _T4:16'
_init_QueueItem:
    _T0 = _global_addr
    _T9 = 0
    _T10 = 0
    _T11 = (_T9 < _T10)
    if (_T11 == 0) branch _L5
    _T12 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T12
    call _PrintString
    call _Halt
_L5:
    _T13 = 4
    _T14 = (_T9 * _T13)
    _T15 = (_T1 + _T14)
    _T16 = *(_T15 + 0)
    _T17 = 4
    _T18 = (_T9 * _T17)
    _T19 = (_T1 + _T18)
    *(_T19 + 0) = _T2
    _T20 = 1
    _T21 = 0
    _T22 = (_T20 < _T21)
    if (_T22 == 0) branch _L17
    _T23 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T23
    call _PrintString
    call _Halt
_L17:
    _T24 = 4
    _T25 = (_T20 * _T24)
    _T26 = (_T1 + _T25)
    _T27 = *(_T26 + 0)
    _T28 = 4
    _T29 = (_T20 * _T28)
    _T30 = (_T1 + _T29)
    *(_T30 + 0) = _T3
    _T31 = 2
    _T32 = 0
    _T33 = (_T31 < _T32)
    if (_T33 == 0) branch _L18
    _T34 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T34
    call _PrintString
    call _Halt
_L18:
    _T35 = 4
    _T36 = (_T31 * _T35)
    _T37 = (_T1 + _T36)
    _T38 = *(_T37 + 0)
    _T39 = 4
    _T40 = (_T31 * _T39)
    _T41 = (_T1 + _T40)
    *(_T41 + 0) = _T4
    _T42 = 2
    _T43 = 0
    _T44 = (_T42 < _T43)
    if (_T44 == 0) branch _L19
    _T45 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T45
    call _PrintString
    call _Halt
_L19:
    _T46 = 4
    _T47 = (_T42 * _T46)
    _T48 = (_T3 + _T47)
    _T49 = *(_T48 + 0)
    _T50 = 4
    _T51 = (_T42 * _T50)
    _T52 = (_T3 + _T51)
    *(_T52 + 0) = _T1
    _T53 = 1
    _T54 = 0
    _T55 = (_T53 < _T54)
    if (_T55 == 0) branch _L20
    _T56 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T56
    call _PrintString
    call _Halt
_L20:
    _T57 = 4
    _T58 = (_T53 * _T57)
    _T59 = (_T4 + _T58)
    _T60 = *(_T59 + 0)
    _T61 = 4
    _T62 = (_T53 * _T61)
    _T63 = (_T4 + _T62)
    *(_T63 + 0) = _T1
}

FUNCTION(main) {
memo ''
main:
    _T0 = _global_addr
    _T6 = 10
    *(_T0 + 0) = _T6
    _T7 = 4
    *(_T0 + 4) = _T7
    _T8 = 17
    *(_T0 + 8) = _T8
    call _init_Queue
    _T65 = 0
    _T64 = _T65
    branch _L21
_L22:
    _T66 = 1
    _T67 = (_T64 + _T66)
    _T64 = _T67
_L21:
    _T68 = *(_T0 + 0)
    _T69 = (_T64 < _T68)
    if (_T69 == 0) branch _L23
    parm _T64
    call _enqueue
    branch _L22
_L23:
    _T70 = 0
    _T64 = _T70
    branch _L24
_L25:
    _T71 = 1
    _T72 = (_T64 + _T71)
    _T64 = _T72
_L24:
    _T73 = *(_T0 + 4)
    _T74 = (_T64 < _T73)
    if (_T74 == 0) branch _L26
    _T75 =  call _dequeue
    parm _T75
    call _PrintInt
    _T76 = " "
    parm _T76
    call _PrintString
    branch _L25
_L26:
    _T77 = "\n"
    parm _T77
    call _PrintString
    _T78 = 0
    _T64 = _T78
    branch _L27
_L28:
    _T79 = 1
    _T80 = (_T64 + _T79)
    _T64 = _T80
_L27:
    _T81 = *(_T0 + 0)
    _T82 = (_T64 < _T81)
    if (_T82 == 0) branch _L29
    parm _T64
    call _enqueue
    branch _L28
_L29:
    _T83 = 0
    _T64 = _T83
    branch _L30
_L31:
    _T84 = 1
    _T85 = (_T64 + _T84)
    _T64 = _T85
_L30:
    _T86 = *(_T0 + 8)
    _T87 = (_T64 < _T86)
    if (_T87 == 0) branch _L32
    _T88 =  call _dequeue
    parm _T88
    call _PrintInt
    _T89 = " "
    parm _T89
    call _PrintString
    branch _L31
_L32:
    _T90 = "\n"
    parm _T90
    call _PrintString
}

FUNCTION(_init_Queue) {
memo ''
_init_Queue:
    _T0 = _global_addr
    _T91 = *(_T0 + 16)
    _T92 = 4
    _T93 = 2
    _T94 = 4
    _T95 = (_T93 * _T94)
    _T96 = (_T92 + _T95)
    _T97 = 0
    _T98 = (_T96 < _T97)
    if (_T98 == 0) branch _L33
    _T99 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T99
    call _PrintString
    call _Halt
_L33:
    parm _T96
    _T100 =  call _Alloc
    *(_T0 + 16) = _T100
    _T101 = *(_T0 + 16)
    _T102 = 0
    _T103 = *(_T0 + 16)
    _T104 = *(_T0 + 16)
    parm _T101
    parm _T102
    parm _T103
    parm _T104
    call _init_QueueItem
}

FUNCTION(_enqueue) {
memo '_T5:4'
_enqueue:
    _T0 = _global_addr
    _T106 = 4
    _T107 = 2
    _T108 = 4
    _T109 = (_T107 * _T108)
    _T110 = (_T106 + _T109)
    _T111 = 0
    _T112 = (_T110 < _T111)
    if (_T112 == 0) branch _L34
    _T113 = "Decaf runtime error: Cannot create negative-sized array\n"
    parm _T113
    call _PrintString
    call _Halt
_L34:
    parm _T110
    _T114 =  call _Alloc
    _T105 = _T114
    _T115 = *(_T0 + 16)
    _T116 = 1
    _T117 = 0
    _T118 = (_T116 < _T117)
    if (_T118 == 0) branch _L35
    _T119 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T119
    call _PrintString
    call _Halt
_L35:
    _T120 = 4
    _T121 = (_T116 * _T120)
    _T122 = (_T115 + _T121)
    _T123 = *(_T122 + 0)
    _T124 = *(_T0 + 16)
    parm _T105
    parm _T5
    parm _T123
    parm _T124
    call _init_QueueItem
}

FUNCTION(_dequeue) {
memo ''
_dequeue:
    _T0 = _global_addr
    _T125 = *(_T0 + 16)
    _T126 = 2
    _T127 = 0
    _T128 = (_T126 < _T127)
    if (_T128 == 0) branch _L36
    _T129 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T129
    call _PrintString
    call _Halt
_L36:
    _T130 = 4
    _T131 = (_T126 * _T130)
    _T132 = (_T125 + _T131)
    _T133 = *(_T132 + 0)
    _T134 = *(_T0 + 16)
    _T135 = (_T133 == _T134)
    if (_T135 == 0) branch _L37
    _T136 = "Queue Is Empty"
    parm _T136
    call _PrintString
    _T137 = 0
    return _T137
    branch _L38
_L37:
    _T139 = *(_T0 + 16)
    _T140 = 2
    _T141 = 0
    _T142 = (_T140 < _T141)
    if (_T142 == 0) branch _L39
    _T143 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T143
    call _PrintString
    call _Halt
_L39:
    _T144 = 4
    _T145 = (_T140 * _T144)
    _T146 = (_T139 + _T145)
    _T147 = *(_T146 + 0)
    _T138 = _T147
    _T149 = 0
    _T150 = 0
    _T151 = (_T149 < _T150)
    if (_T151 == 0) branch _L40
    _T152 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T152
    call _PrintString
    call _Halt
_L40:
    _T153 = 4
    _T154 = (_T149 * _T153)
    _T155 = (_T138 + _T154)
    _T156 = *(_T155 + 0)
    _T148 = _T156
    _T157 = 2
    _T158 = 0
    _T159 = (_T157 < _T158)
    if (_T159 == 0) branch _L41
    _T160 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T160
    call _PrintString
    call _Halt
_L41:
    _T161 = 4
    _T162 = (_T157 * _T161)
    _T163 = (_T138 + _T162)
    _T164 = *(_T163 + 0)
    _T165 = 1
    _T166 = 0
    _T167 = (_T165 < _T166)
    if (_T167 == 0) branch _L42
    _T168 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T168
    call _PrintString
    call _Halt
_L42:
    _T169 = 4
    _T170 = (_T165 * _T169)
    _T171 = (_T164 + _T170)
    _T172 = *(_T171 + 0)
    _T173 = 1
    _T174 = 0
    _T175 = (_T173 < _T174)
    if (_T175 == 0) branch _L43
    _T176 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T176
    call _PrintString
    call _Halt
_L43:
    _T177 = 4
    _T178 = (_T173 * _T177)
    _T179 = (_T138 + _T178)
    _T180 = *(_T179 + 0)
    _T181 = 4
    _T182 = (_T165 * _T181)
    _T183 = (_T164 + _T182)
    *(_T183 + 0) = _T180
    _T184 = 1
    _T185 = 0
    _T186 = (_T184 < _T185)
    if (_T186 == 0) branch _L44
    _T187 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T187
    call _PrintString
    call _Halt
_L44:
    _T188 = 4
    _T189 = (_T184 * _T188)
    _T190 = (_T138 + _T189)
    _T191 = *(_T190 + 0)
    _T192 = 2
    _T193 = 0
    _T194 = (_T192 < _T193)
    if (_T194 == 0) branch _L45
    _T195 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T195
    call _PrintString
    call _Halt
_L45:
    _T196 = 4
    _T197 = (_T192 * _T196)
    _T198 = (_T191 + _T197)
    _T199 = *(_T198 + 0)
    _T200 = 2
    _T201 = 0
    _T202 = (_T200 < _T201)
    if (_T202 == 0) branch _L46
    _T203 = "Decaf runtime error: Array subscript out of bounds\n"
    parm _T203
    call _PrintString
    call _Halt
_L46:
    _T204 = 4
    _T205 = (_T200 * _T204)
    _T206 = (_T138 + _T205)
    _T207 = *(_T206 + 0)
    _T208 = 4
    _T209 = (_T192 * _T208)
    _T210 = (_T191 + _T209)
    *(_T210 + 0) = _T207
    return _T148
_L38:
}

