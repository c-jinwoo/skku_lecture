package edu.skku.cs.pa2

// 데이터 클래스
data class DataLoginRequest(var username: String?= null)
data class DataLoginResponse(var success: Boolean?= null)
data class MapList(var name: String?= null, var size: Integer?= null)
data class MapInfo(var maze: String?= null)
data class Pos(var user: Int?= null, var user_rot: Int?= null, var hint: Int?= null)
data class coord(var x: Int, val y:Int)

// 게임 변수
val size = 350
val margin = 3
val mask_up = 0x8
val mask_left = 0x4
val mask_down = 0x2
val mask_right = 0x1
var turn = 0                                // 턴 수
var pos = Pos(0, 0, -1)    // 이미지 정보
var hint_cnt = 0                            // 힌트 갯수
var cell_cnt = 0                            // 각 변 셀 갯수
var gameClear = false                       // 해당 게임 클리어 여부