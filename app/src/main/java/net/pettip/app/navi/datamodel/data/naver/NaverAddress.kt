package net.pettip.app.navi.datamodel.data.naver


import com.google.gson.annotations.SerializedName

data class NaverAddress(
    @SerializedName("results")
    var results: List<Result?>?,
    @SerializedName("status")
    var status: Status?
) {
    data class Result(
        @SerializedName("code")
        var code: Code?,
        @SerializedName("land")
        var land: Land?,
        @SerializedName("name")
        var name: String?, // admcode
        @SerializedName("region")
        var region: Region?
    ) {
        data class Code(
            @SerializedName("id")
            var id: String?, // 1120069000
            @SerializedName("mappingId")
            var mappingId: String?, // 09200690
            @SerializedName("type")
            var type: String? // A
        )

        data class Land(
            @SerializedName("addition0")
            var addition0: Addition0?,
            @SerializedName("addition1")
            var addition1: Addition1?,
            @SerializedName("addition2")
            var addition2: Addition2?,
            @SerializedName("addition3")
            var addition3: Addition3?,
            @SerializedName("addition4")
            var addition4: Addition4?,
            @SerializedName("coords")
            var coords: Coords?,
            @SerializedName("name")
            var name: String?, // 아차산로17길
            @SerializedName("number1")
            var number1: String?, // 21
            @SerializedName("number2")
            var number2: String?,
            @SerializedName("type")
            var type: String?
        ) {
            data class Addition0(
                @SerializedName("type")
                var type: String?, // building
                @SerializedName("value")
                var value: String? // 성수초등학교
            )

            data class Addition1(
                @SerializedName("type")
                var type: String?, // zipcode
                @SerializedName("value")
                var value: String? // 04798
            )

            data class Addition2(
                @SerializedName("type")
                var type: String?, // roadGroupCode
                @SerializedName("value")
                var value: String? // 112004109391
            )

            data class Addition3(
                @SerializedName("type")
                var type: String?,
                @SerializedName("value")
                var value: String?
            )

            data class Addition4(
                @SerializedName("type")
                var type: String?,
                @SerializedName("value")
                var value: String?
            )

            data class Coords(
                @SerializedName("center")
                var center: Center?
            ) {
                data class Center(
                    @SerializedName("crs")
                    var crs: String?,
                    @SerializedName("x")
                    var x: Double?, // 0.0
                    @SerializedName("y")
                    var y: Double? // 0.0
                )
            }
        }

        data class Region(
            @SerializedName("area0")
            var area0: Area0?,
            @SerializedName("area1")
            var area1: Area1?,
            @SerializedName("area2")
            var area2: Area2?,
            @SerializedName("area3")
            var area3: Area3?,
            @SerializedName("area4")
            var area4: Area4?
        ) {
            data class Area0(
                @SerializedName("coords")
                var coords: Coords?,
                @SerializedName("name")
                var name: String? // kr
            ) {
                data class Coords(
                    @SerializedName("center")
                    var center: Center?
                ) {
                    data class Center(
                        @SerializedName("crs")
                        var crs: String?,
                        @SerializedName("x")
                        var x: Double?, // 0.0
                        @SerializedName("y")
                        var y: Double? // 0.0
                    )
                }
            }

            data class Area1(
                @SerializedName("alias")
                var alias: String?, // 서울
                @SerializedName("coords")
                var coords: Coords?,
                @SerializedName("name")
                var name: String? // 서울특별시
            ) {
                data class Coords(
                    @SerializedName("center")
                    var center: Center?
                ) {
                    data class Center(
                        @SerializedName("crs")
                        var crs: String?, // EPSG:4326
                        @SerializedName("x")
                        var x: Double?, // 126.978388
                        @SerializedName("y")
                        var y: Double? // 37.56661
                    )
                }
            }

            data class Area2(
                @SerializedName("coords")
                var coords: Coords?,
                @SerializedName("name")
                var name: String? // 성동구
            ) {
                data class Coords(
                    @SerializedName("center")
                    var center: Center?
                ) {
                    data class Center(
                        @SerializedName("crs")
                        var crs: String?, // EPSG:4326
                        @SerializedName("x")
                        var x: Double?, // 127.036821
                        @SerializedName("y")
                        var y: Double? // 37.563456
                    )
                }
            }

            data class Area3(
                @SerializedName("coords")
                var coords: Coords?,
                @SerializedName("name")
                var name: String? // 성수2가3동
            ) {
                data class Coords(
                    @SerializedName("center")
                    var center: Center?
                ) {
                    data class Center(
                        @SerializedName("crs")
                        var crs: String?, // EPSG:4326
                        @SerializedName("x")
                        var x: Double?, // 127.055265
                        @SerializedName("y")
                        var y: Double? // 37.548222
                    )
                }
            }

            data class Area4(
                @SerializedName("coords")
                var coords: Coords?,
                @SerializedName("name")
                var name: String?
            ) {
                data class Coords(
                    @SerializedName("center")
                    var center: Center?
                ) {
                    data class Center(
                        @SerializedName("crs")
                        var crs: String?,
                        @SerializedName("x")
                        var x: Double?, // 0.0
                        @SerializedName("y")
                        var y: Double? // 0.0
                    )
                }
            }
        }
    }

    data class Status(
        @SerializedName("code")
        var code: Int?, // 0
        @SerializedName("message")
        var message: String?, // done
        @SerializedName("name")
        var name: String? // ok
    )
}