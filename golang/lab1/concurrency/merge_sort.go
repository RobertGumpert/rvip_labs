package concurrency

import (
	"sync"
)

var(
	mutex = new(sync.Mutex)
)

func Sort(matrix [][]int) {
	for index := range matrix {
		go mergeSort(matrix, index)
	}
}

func mergeSort(matrix [][]int, index int) {
	var(
		input = matrix[index]
		result = make(chan []int)
	)
	go sorted(input, result)
	sorted := <- result
	//
	mutex.Lock()
	defer mutex.Unlock()
	//
	matrix[index] = sorted
}

func sorted(input []int, result chan []int) {
	if len(input) <= 1 {
		result <- input
		return
	}
	//
	middle := int(len(input) / 2)
	//
	leftChan := make(chan []int)
	rightChan := make(chan []int)
	//
	go sorted(input[:middle], leftChan)
	go sorted(input[middle:], rightChan)
	//
	sortedLeftArray := <-leftChan
	sortedRightArray := <-rightChan
	//
	sortedMergeArray := merge(sortedLeftArray, sortedRightArray)
	result <- sortedMergeArray
}


func merge(right, left []int) (sorted []int) {
	//
	size, i, j := len(right)+len(left), len(right)-1, len(left)-1
	sorted = make([]int, size)
	//
	for k := size - 1; k >= 0; k-- {
		switch true {
		case i == -1:
			sorted[k] = left[j]
			j--
		case j == -1:
			sorted[k] = right[i]
			i--
		case right[i] > left[j]:
			sorted[k] = left[j]
			j--
		case right[i] < left[j]:
			sorted[k] = right[i]
			i--
		case right[i] == left[j]:
			sorted[k] = left[j]
			j--
		}
	}
	//
	return sorted
}
