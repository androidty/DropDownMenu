package com.ty.dropdowndemo.bean

class DropBean {
    var hotBrands: List<HotBrandsBean>? = null
    var group: List<GroupBean>? = null
    var isSelect: Boolean? = false
    var name: String? = null
    var letter: String? = null

    constructor(name: String) {
        this.name = name
    }

    class HotBrandsBean {
        /**
         * id : 1
         * name : 川崎
         * status : null
         * brandLogo : http://qibeitech.oss-cn-hangzhou.aliyuncs.com/garage/brand/8a8acddf91cf4e07a8941d133275bbcd.jpg
         * firstAlphabet : C
         */
        var id = 0
        var name: String? = null
        var status: Any? = null
        var brandLogo: String? = null
        var firstAlphabet: String? = null

    }

    class GroupBean {
        /**
         * firstAlphabet : A
         * brands : [{"id":24,"name":"阿普利亚","status":"上架中","brandLogo":"http://qibeitech.oss-cn-hangzhou.aliyuncs.com/garage/brand/2f9f44e1006d4d82ae73ac1e53d6ff30.jpg","firstAlphabet":"A"},{"id":70,"name":"Adiva","status":"上架中","brandLogo":"http://qibeitech.oss-cn-hangzhou.aliyuncs.com/garage/brand/14e777dedb4b476289d013e2e4886e8d.jpg","firstAlphabet":"A"}]
         */
        var firstAlphabet: String? = null
        var brands: List<BrandsBean>? = null

        class BrandsBean {
            /**
             * id : 24
             * name : 阿普利亚
             * status : 上架中
             * brandLogo : http://qibeitech.oss-cn-hangzhou.aliyuncs.com/garage/brand/2f9f44e1006d4d82ae73ac1e53d6ff30.jpg
             * firstAlphabet : A
             */
            var id = 0
            var name: String? = null
            var status: String? = null
            var brandLogo: String? = null
            var firstAlphabet: String? = null

        }
    }
}