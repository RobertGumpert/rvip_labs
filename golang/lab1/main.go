package main

import (
	"./concurrency"
	"./signlethread"
	"fmt"
	"time"
)

/*
	Упорядочить строки матрицы
	по убыванию наибольших элементов.
*/

var (
	matrix1 = [][]int{
		{28,   4,  98,   3,  95},
		{85,  10,  85,  11,  44},
		{100, 35,  51,  26,  30},
		{29,  50,  87,  55,  92},
		{42,  100, 78,  67,   9},
	}
	matrix2 = [][]int{
		{28,   4,  98,   3,  95},
		{85,  10,  85,  11,  44},
		{100, 35,  51,  26,  30},
		{29,  50,  87,  55,  92},
		{42,  100, 78,  67,   9},
	}
)

func main() {
	//
	signlethread.Sort(matrix1)
	fmt.Println(matrix1)
	//
	concurrency.Sort(matrix2)
	time.Sleep(5*time.Second)
	fmt.Println(matrix2)

}
