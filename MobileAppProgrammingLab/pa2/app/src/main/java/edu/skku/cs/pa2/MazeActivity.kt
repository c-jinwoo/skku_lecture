package edu.skku.cs.pa2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*

class MazeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        // 컴포넌트
        var gridView = findViewById<GridView>(R.id.GridViewMap)
        val tv_turn = findViewById<TextView>(R.id.textViewTurn)
        val btn_left = findViewById<Button>(R.id.buttonLeft)
        val btn_right = findViewById<Button>(R.id.buttonRight)
        val btn_up = findViewById<Button>(R.id.buttonUp)
        val btn_down = findViewById<Button>(R.id.buttonDown)
        val btn_hint = findViewById<Button>(R.id.buttonHint)

        // 요청 설정
        val client = OkHttpClient()
        val host = "http://121.169.12.99:10099/maze/map?name=" + intent.getStringExtra(MazeSelectionActivity.MAP_NAME)
        val req = Request.Builder().url(host).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    // 값 수신
                    val data = response.body!!.string()
                    val result = Gson().fromJson(data, MapInfo::class.java).maze.toString()
                    val resultArr = result.split("\n")

                    // 초기화
                    gameSet(resultArr[0].toInt())
                    val typeArr = Array(cell_cnt * cell_cnt){0}                                // 각 셀 타입 저장용 배열
                    val mazeArr = Array(cell_cnt * 2 - 1){IntArray(cell_cnt * 2 - 1){1} }  // 미로 저장용 배열

                    // 각 셀 타입 저장
                    for (i in 1 .. cell_cnt){
                        val resultArrSplit = resultArr[i].split(" ")

                        for (j in 0 until cell_cnt) {
                            typeArr[(i - 1) * cell_cnt + j] = resultArrSplit[j].toInt()
                        }
                    }

                    // 미로 생성
                    for (i in 0 until cell_cnt){
                        for (j in 0 until cell_cnt){
                            mazeArr[i * 2][j * 2] = 0
                            val idx = i * cell_cnt + j

                            if(j < cell_cnt - 1) {
                                if ((typeArr[idx] and mask_right == 0) and (typeArr[idx + 1] and mask_left == 0)) {
                                    mazeArr[i * 2][j * 2 + 1] = 0
                                }
                            }
                            if(i < cell_cnt - 1){
                                if ((typeArr[idx] and mask_down == 0) and (typeArr[idx + cell_cnt] and mask_up == 0)) {
                                    mazeArr[i * 2 + 1][j * 2] = 0
                                }
                            }
                        }
                    }

                    // 코루틴
                    CoroutineScope(Dispatchers.Main).launch {
                        tv_turn.text = "Turn : $turn"
                        gridView.numColumns = cell_cnt
                        gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)
                    }

                    // 버튼 동작 설정
                    btn_left.setOnClickListener {
                        if(!gameClear) {
                            pos.user_rot = 3

                            if (typeArr[pos.user!!] and mask_left == 0) {
                                pos.user = pos.user!! - 1
                                turn += 1
                                tv_turn.text = "Turn : $turn"
                            }

                            btnCommonFunc()
                            gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)
                        }
                    }

                    btn_right.setOnClickListener {
                        if(!gameClear) {
                            pos.user_rot = 1

                            if (typeArr[pos.user!!] and mask_right == 0) {
                                pos.user = pos.user!! + 1
                                turn += 1
                                tv_turn.text = "Turn : $turn"
                            }

                            btnCommonFunc()
                            gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)
                        }
                    }

                    btn_up.setOnClickListener {
                        if(!gameClear) {
                            pos.user_rot = 0

                            if (typeArr[pos.user!!] and mask_up == 0) {
                                pos.user = pos.user!! - cell_cnt
                                turn += 1
                                tv_turn.text = "Turn : $turn"
                            }

                            btnCommonFunc()
                            gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)
                        }
                    }

                    btn_down.setOnClickListener {
                        if(!gameClear) {
                            pos.user_rot = 2

                            if (typeArr[pos.user!!] and mask_down == 0) {
                                pos.user = pos.user!! + cell_cnt
                                turn += 1
                                tv_turn.text = "Turn : $turn"
                            }

                            btnCommonFunc()
                            gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)
                        }
                    }

                    btn_hint.setOnClickListener {
                        if(!gameClear) {
                            if (hint_cnt > 0) {
                                val rowPos = (pos.user!! / cell_cnt) * 2
                                val colPos = (pos.user!! % cell_cnt) * 2
                                val tempPath =
                                    getPosition(mazeArr, coord(rowPos!!.toInt(), colPos!!.toInt()))
                                val nextPath = getPosition(mazeArr, tempPath!!)

                                pos.hint = (nextPath!!.x / 2) * cell_cnt + nextPath!!.y / 2
                                gridView.adapter = MazeAdapter(this@MazeActivity, typeArr, pos)

                                hint_cnt--
                            }
                        }
                    }
                }
            }
        })
    }

    fun gameSet(size: Int){
        turn = 0
        hint_cnt = 1
        cell_cnt = size
        gameClear = false
        pos = Pos(0, 0, -1)
    }

    fun btnCommonFunc(){
        if (pos.user == cell_cnt * cell_cnt - 1) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    this@MazeActivity,
                    "Finish!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            gameClear = true
        }
        else if (pos.user == pos.hint) {
            pos.hint = -1
        }
    }

    fun getPosition(maze: Array<IntArray>, start: coord): coord? {
        val dx = arrayOf(-1, 1, 0, 0)
        val dy = arrayOf(0, 0, -1, 1)
        val queue = LinkedList<coord>()
        val size = cell_cnt * 2 - 1
        val destination = coord(size - 1, size - 1)
        val visited = Array(maze.size){BooleanArray(maze[0].size)}
        val prev = Array(maze.size){Array<coord?>(maze[0].size){null}}

        queue.add(start)
        visited[start.x][start.y] = true

        while (queue.isNotEmpty()) {
            val current = queue.remove()

            if (current == destination) {
                var next = current
                while(prev[next.x][next.y] != start) {
                    next = prev[next.x][next.y]!!
                }
                return next
            }

            for (i in 0 until 4) {
                val nx = current.x + dx[i]
                val ny = current.y + dy[i]

                if (nx in maze.indices && ny in 0 until maze[0].size && maze[nx][ny] == 0 && !visited[nx][ny]) {
                    val next = coord(nx, ny)
                    queue.add(next)
                    prev[nx][ny] = current
                    visited[nx][ny] = true
                }
            }
        }
        return null
    }
}