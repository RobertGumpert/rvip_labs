package signlethread

func Sort(matrix [][]int) {
	for index := range matrix {
		sorted := mergeSort(matrix[index])
		matrix[index] = sorted
	}
}

func mergeSort(input []int) []int {
	if len(input) <= 1 {
		return input
	}
	var (
		middle = int(len(input) / 2)
		left   = mergeSort(input[:middle])
		right  = mergeSort(input[middle:])
	)
	return merge(right, left)
}

func merge(right, left []int) (sorted []int) {
	//
	size, i, j := len(right)+len(left), len(right)-1, len(left)-1
	sorted = make([]int, size)
	//
	for k := size-1; k >= 0; k-- {
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

