import mpi.*;


import java.util.Arrays;

// ЧТО ДЕЛАЕТ ЛИБА?
//      По сути она запускает несколько экземпляров написанной программы.
//      Кол-во указывается с помощью флага -np в передаваемых аргументах программы.
//
// RUN -> EDIT CONFIGURATION ->
//                              VM options :
//                                      -jar $MPJ_HOME$\lib\starter.jar -np 6
//                              Environment variables :
//                                      MPJ_HOME=C:/Labs/rvip/rvip_labs/java/labs_libs/mpj
//
public class Main {


    private static final int MASTER_PROCESS_RANK = 0;
    private static int[][] array = new int[][]{
            {28, 4, 98, 3, 95},
            {85, 10, 85, 11, 44},
            {100, 35, 51, 26, 30},
            {29, 50, 87, 55, 92},
            {42, 100, 78, 67, 9},
    };

    private static int[] matrixToSlice() {
        int[] slice = new int[array.length * array[0].length];
        int sliceIndexPosition = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                slice[sliceIndexPosition++] = array[i][j];
            }
        }
        return slice;
    }

    private static void updateMatrix(int[] sortedSlice) {
        int position = 0;
        int row = 0;
        for (int i = 0; i < sortedSlice.length; i++) {
            if (position == 5) {
                row++;
                position = 0;
            }
            array[row][position] = sortedSlice[i];
            position++;
        }
    }

    public static void main(String[] args) throws Exception {
        MPI.Init(args);
        //
        // Ранг текущего, запущеного процесса.
        //
        int currentExecutableProcessRank = MPI.COMM_WORLD.Rank();
        //
        // Длина ячейки в буффере, выделеной для каждого процесса.
        //
        int lengthCellBuffer = array[0].length;
        //
        if (currentExecutableProcessRank == MASTER_PROCESS_RANK) {
            //
            int countExecutableProcesses = MPI.COMM_WORLD.Size();
            System.out.println("Master is started!");
            System.out.println("Count process : " + countExecutableProcesses);
            //
            // Переваричиваем матрицу в одномерный массив,
            // он будет выступать буфером.
            //
            int[] sendMessageBuffer = matrixToSlice();
            //
            // Т.к. это мастер, то итерируемся с 1, по всему кол-ву процессов.
            //
            long start = System.currentTimeMillis();
            for (int process = 1; process < countExecutableProcesses; process++) {
                //
                // Вычисляем позицию начала ячейки, выделенной для процесса в буффере.
                // Начиная с этой позиции и до конца ячейки,
                // это будет являться сообщением, которое получит процесс.
                //
                //      BUFFER : [28, 4, 98, 3, 95, 85, 10, 85, 11, 44, 100, 35, 51, 26, 30, 29, 50, 87, 55, 92, 42, 100, 78, 67, 9]
                //      positionStartCellBuffer : (3 - 1) * 5 = 10 -> позицию начала ячейки.
                //      С позиции 10 процесс считает 5 следующий элементов -> [100, 35, 51, 26, 30].
                //
                int positionStartCellBuffer = (process - 1) * lengthCellBuffer;
                //
                // Отправляем сообщение процессам.
                //
                MPI.COMM_WORLD.Send(sendMessageBuffer, positionStartCellBuffer, lengthCellBuffer, MPI.INT, process, 100);
            }
            //
            // Т.к. это мастер, то итерируемся с 1, по всему кол-ву процессов.
            //
            for (int process = 1; process < countExecutableProcesses; process++) {
                //
                // Вычисляем позицию начала ячейки, выделенной для процесса в буффере.
                // Начиная с этой позиции и до конца ячейки,
                // это будет являться сообщением, которое получит процесс.
                //
                //      BUFFER : [28, 4, 98, 3, 95, 85, 10, 85, 11, 44, 100, 35, 51, 26, 30, 29, 50, 87, 55, 92, 42, 100, 78, 67, 9]
                //      positionStartCellBuffer : (3 - 1) * 5 = 10 -> позицию начала ячейки.
                //      С позиции 10 процесс считает 5 следующий элементов -> [100, 35, 51, 26, 30].
                //
                int positionStartCellBuffer = (process - 1) * lengthCellBuffer;
                //
                // Получаем сообщение от процессов.
                //
                MPI.COMM_WORLD.Recv(sendMessageBuffer, positionStartCellBuffer, lengthCellBuffer, MPI.INT, process, 100);
            }
            //
            long finish = System.currentTimeMillis();
            float timeElapsed = (finish - start) / 100F;
            //
            updateMatrix(sendMessageBuffer);
            System.out.println("Result : " + Arrays.deepToString(array));
            System.out.println("Result in time elapsed : " + timeElapsed);
        } else {
            //
            // Полученное сообщение от мастера.
            //
            int[] receiveMessageBuffer = new int[lengthCellBuffer];
            MPI.COMM_WORLD.Recv(receiveMessageBuffer, 0, lengthCellBuffer, MPI.INT, MASTER_PROCESS_RANK, 100);
            System.out.println("Process : " + currentExecutableProcessRank + ", receive message : " + Arrays.toString(receiveMessageBuffer));
            //
            // Сортируем полученное сообщение и отправляем мастеру.
            //
            receiveMessageBuffer = mergeSort(receiveMessageBuffer);
            MPI.COMM_WORLD.Send(receiveMessageBuffer, 0, lengthCellBuffer, MPI.INT, MASTER_PROCESS_RANK, 100);
        }

        MPI.Finalize();
    }

    private static int[] mergeSort(int[] input) {
        if (input.length <= 1) {
            return input;
        }
        //
        int middle = input.length / 2;
        int[] left = new int[middle];
        int[] right = new int[input.length - middle];
        //
        for (int i = 0; i < middle; i++) {
            left[i] = input[i];
        }
        for (int i = middle; i < input.length; i++) {
            right[i - middle] = input[i];
        }
        //
        return merge(mergeSort(right), mergeSort(left));
    }

    private static int[] merge(int[] right, int[] left) {
        //
        int size = right.length + left.length;
        int i = right.length - 1;
        int j = left.length - 1;
        int[] sorted = new int[size];
        //
        for (int k = size - 1; k >= 0; k--) {
            if (i == -1) {
                sorted[k] = left[j];
                j--;
                continue;
            }
            if (j == -1) {
                sorted[k] = right[i];
                i--;
                continue;
            }
            if (right[i] > left[j]) {
                sorted[k] = left[j];
                j--;
                continue;
            }
            if (right[i] < left[j]) {
                sorted[k] = right[i];
                i--;
                continue;
            }
            if (right[i] == left[j]) {
                sorted[k] = left[j];
                j--;
            }
        }
        return sorted;
    }
}
