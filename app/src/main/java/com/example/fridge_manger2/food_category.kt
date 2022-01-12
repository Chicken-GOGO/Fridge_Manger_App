package com.example.fridge_manger2

class food_category() {

    val bigclass= arrayOf("cooking","dairy","delicatessen","dessert","drink","fruit","grains","meat","protein","vegetable")
    //small classes
    val cooking= arrayOf("butter","chili","jam","kimchi","olive_oil","soy_sauce")
    val dairy= arrayOf("cheese","milk","yogurt")
    val delicatessen= arrayOf("baozi","dish","dumpling","lunch_box","noodles","raw_dumpling","rice_ball","soup")
    val dessert=arrayOf("apple_pie","birthday_cake","biscuit","cake","candy","chocolate_bar","dount","ice_bar","ice_cream"
        ,"panna_cotta","pudding")
    val drink= arrayOf("beer","bottle_drink","canned_drink","hand_shake","juice","soy-milk","water")
    val fruit= arrayOf("apple","banana","cherry","lemon","mango","orange","peach","pear","pineapple","strawberry","watermelon")
    val grains=arrayOf("bread","corn","croissant","raw_rice","rice","sweet_potato","toast")
    val meat=arrayOf("beef","chicken","fish","mutton","pork")
    val protein=arrayOf("egg","tofu")
    val vegatable= arrayOf("sweet_pepper","broccoli","cabbage","carrot","grape","guava","lettuce"
        ,"onion","potato")
    val food=listOf(
        AddFood.FoodInfo("banana"),
        AddFood.FoodInfo("spinach"),
        AddFood.FoodInfo("chicken"),
        AddFood.FoodInfo("strawberry"),
        AddFood.FoodInfo("spinach")
    )
    //    val cooking_icon= listOf(
//        AddFood.FoodInfo("butter"),
//        AddFood.FoodInfo("dairy"),
//        AddFood.FoodInfo("delicatessen"),
//        AddFood.FoodInfo("dessert"),
//        AddFood.FoodInfo("drink"),
//        AddFood.FoodInfo("fruit"),
//        AddFood.FoodInfo("grains"),
//        AddFood.FoodInfo("meat"),
//        AddFood.FoodInfo("protein"),
//        AddFood.FoodInfo("vegetable"))
    val iconid= listOf(R.id.banana,R.id.spinach,R.id.chicken,R.id.strawberry,R.id.spinach)
    val big_iconid=listOf(R.drawable.cooking,R.drawable.dairy,R.drawable.delicatessen,R.drawable.dessert
        ,R.drawable.drink,R.drawable.fruit)

}