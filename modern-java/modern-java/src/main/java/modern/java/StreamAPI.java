package modern.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * StreamAPIとラムダ式を理解する
 * ラムダとは概念的に関数をJavaが表現できるようにしてくれたもの
 * （関数＝object）
 * Stream操作
 * 1. Stream生成
 * 2. 中間操作 Streamを返す。
 * 3. 終端操作 コレクションや値を返す。
 * ラムダ式はすべて終端操作が実行される時点で実行される（遅延評価）
 * 終端操作メソッドを行うとStreamオブジェクトはクローズされる（使えなくなる）
 */
public class StreamAPI {
    public static void main(String[] args) {
        System.out.println("*無名クラスDEMO*********");
        anonymousClassDEMO();
        System.out.println("*ラムダDEMO*************");
        lambdaDEMO();
        System.out.println("*ConsumerDEMO***********");
        consumerDEMO();
        System.out.println("*SupplierDEMO***********");
        supplierDEMO();
        System.out.println("*PredicateDEMO**********");
        predicateDEMO();
        System.out.println("*FunctionDEMO***********");
        functionDEMO();
        System.out.println("*終端処理DEMO***********");
        streamTerminal();
        System.out.println("*中間処理DEMO***********");
        streamIntermediate();

    }

    /**
     * Funcインターフェースを実装した無名クラスをローカルクラスとして宣言
     */
    static void anonymousClassDEMO() {
        Func f = new Func() {
            public int calc(int x, int y) {
                return x + y;
            }
        };
        System.out.println(f.calc(1, 2));
    }

    /**
     * 無名クラスをラムダ式で書く
     * 左辺から型推論で型を判定、関数型インターフェースなので式まで決定。
     */
    static void lambdaDEMO() {
        Func f = (x, y) -> x + y;
        System.out.println(f.calc(1, 2));
    }

    /**
     * Streamオブジェクトの生成
     * データの集合をラムダ式で処理するためのオブジェクト
     */
    static Stream<String> streamCreate1() {
        List<String> list = Arrays.asList("A", "B", "C", "D");
        Stream<String> stream = list.stream();
        //Filesクラスから行を持つStreamもツクレル
        //直接作るには Stream<String> stream = Stream.of("A", "B");
        return stream;
    }

    static IntStream streamCreate2() {
        int[] arr1 = { 1, 2, 3, 4, 5, 1 };
        IntStream stream = Arrays.stream(arr1);
        //LongStream DoubleStreamもある
        return stream;
    }

    /**
     * 終端操作
     */
    static void streamTerminal() {
        List<String> list = Arrays.asList("A","BC","DEF","GH","I", "A");
        // countはlongで個数を返す
        System.out.println(list.stream().count());
        // min, maxはComparatorを受け取り、次要素を比較し、与えられた式でmax, minを返す
        // 文字数の小さいものは小さいという式
        String max1 = list.stream().max(
                (o1,o2) -> o1.length() - o2.length()
                ).orElse("最大値なし");
        String min1 = list.stream().min(
                (o1,o2) -> o1.length() - o2.length()
                ).orElse("最小値なし");
        System.out.println(max1);
        System.out.println(min1);

        // 戻り型のOptionalはほしい戻り値をラップした型
        // orElseメソッドで中身をとりだせ、nullである場合の戻り値を書ける。(orElseは空のOptionalかを判定している)
        List<String> list2 = Arrays.asList();
        String max2 = list2.stream().max(
                (o1,o2) -> o1.length() - o2.length()
                ).orElse("最大値なし");
        System.out.println(max2);

        int[] arr = {1,2,3,4,5};
        // sumは合計
        System.out.println(Arrays.stream(arr).sum());
        // averageは平均（戻り値OptionalDouble）
        System.out.println(Arrays.stream(arr).average().orElse(0.0));

        // reduceは全てに処理を行う
        //引数１つで戻り値Optional
        Optional<String> optional = list.stream().reduce((x, y) -> x + y);
        System.out.println(optional.orElse("null"));
        //引数２つで戻り値<T> 第一引数は初期値
        String str = list.stream().reduce("Z", (x, y) -> x + y);
        System.out.println(str);

        //<R> r collect(Supplier<R> s, BiConsumer<R,? super T>, a, BiCumsumer<R,R> c)
        // 可変オブジェクトにStream内の要素を集める
        ArrayList<String> list3 =
                list.stream()
                .collect(
                        // Supplierを生成
                        () -> new ArrayList<String>(),
                        // 個々のデータをどう集めるか
                        (l, s) -> l.add(s),
                        // 複数の可変コンテナをどう結合するか(並列処理のため)
                        (l1, l2) -> l1.addAll(l2)
                        );
        list3.forEach(System.out::println);

        // <R,A> R collect(Collector<<? super T, A, R> c) Collectorクラスで簡潔に書ける
        Set<String> set = list.stream().collect(Collectors.toSet());
        set.stream().forEach(System.out::println);
    }

    /**
     * 中間操作
     */
    static void streamIntermediate() {
        List<String> list = Arrays.asList("KLMN", "ABc", "dE", "FGH", "iJ");
        //filterはPredicateを引数にとり、式がTrueのものを集めたStreamを返す。
        list.stream()
            .filter(s -> s.length() >= 3)
            .forEach(System.out::println);

        //mapはFunctionを引数にとり、データを加工してStreamを返す。
        list.stream()
            .map(s -> s.toUpperCase())
            .forEach(System.out::println);
        //mapで型変換
        IntStream iStream =
            list.stream()
                .mapToInt(s -> s.length());
        iStream.forEach(System.out::println);

        //sortedは<T>がComparableなら自然順序でソートしたStreamを返す。
        list.stream()
            .sorted()
            .forEach(System.out::println);
        //Conparatorを引数に入れて、次の要素と比較するソート順もかける
        list.stream()
            .sorted((s1,s2) -> s1.length() - s2.length())
            .forEach(System.out::println);

        Stream<Person> stream = Stream.of(
                new Person("1", "taro"),
                new Person("2", "jiro"),
                new Person("3", "saburo")
                );
        //Comparatorを引数に入れることで<T>のなにを基準にするかを書ける
        stream.sorted(
                    Comparator.comparing(Person::getName) //.reversed() で逆順
             ).forEach(System.out::println);
    }

    /**
     * メソッド参照 class::method
     * 引数なしで書ける
     */
    static void methodRef() {
        List<Integer> l = Arrays.asList(1, 2, 3);
        //l.forEach(i -> System.out.print(i));
        l.forEach(System.out::print);
    }

    /**
     * コンストラクタ参照 class::new
     * オブジェクトを関数型インターフェースに渡せる
     */
    static void constructorRef() {
        Supplier<ArrayList<Integer>> s = ArrayList::new;
        List<Integer> l = s.get();
    }

    /*********************************************
     * java.util.functionパッケージ
     ********************************************/

    /**
     * Consumer<T>.accept(T t)
     * T型を受け取り何かを行う(戻り値なし)
     */
    static void consumerDEMO() {
        List<String> list = Arrays.asList("A", "B", "C");
        //forEachはストリームオブジェクトが持つ要素すべてになんらかの処理を行う
        list.stream().forEach(System.out::println);
        //Tが決まっている IntConsumer, LongConsumer, DoubleConsumerもある
        //2つ引数を受け取れるBiConsumerもある
        //参照型をプリミティブ型を受け取る ObjIntConsumer, ObjLongConsumer, ObjDoubleConsumerもある
    }

    /**
     * Supplier<T>.get()
     * 引数なしでT型の値を返す。
     */
    static void supplierDEMO() {
        Stream
                // generate()がSupplierを求めているので、ラムダ式にできる
                .generate(() -> Math.round(Math.random()))
                //generateは無限にT型（ここではroundの戻り値）の値を生成して保持するので、limitで終わらせる
                .limit(10)
                .forEach(System.out::print);
        System.out.print("\n");
        //Tが決まっている BooleanSupplier, IntSupplier, LongSupplier, DoubleSupplierもある
    }

    /**
     * Predicate<T>.test(T t)
     * T型を受け取りbooleanで返す
     */
    static void predicateDEMO() {
        List<String> list = Arrays.asList("ABC", "DE", "EGHI");
        // TはString 文字が3文字かを判定する
        Predicate<String> p = s -> s.length() >= 3;
        // Streamすべての要素でtest
        boolean all = list.stream().allMatch(p);
        boolean any = list.stream().anyMatch(p);
        boolean none = list.stream().noneMatch(p);
        System.out.println(all);
        System.out.println(any);
        System.out.println(none);
        // Tが決まっている IntPredicate, LongPredicate, DoublePredicateもある
        // 2つの引数を受け取る BiPredicateもある
    }

    /**
     * Function<T,R>.apply(T t)
     * T型で受け取った引数を処理し、R型の値を返す
     */
    static void functionDEMO() {
        /**
         * 〇T型のみ決まっている
         *      IntFunction<R>, LongFunction<R>, DoubleFunction<R>
         * 〇R型のみ決まっている
         *      ToIntFunction<T>, ToLongFunction<T>, ToDoubleFunction<T>
         * 〇T・R型が決まっている
         *      IntToLongFucntion, IntToDoubleFunction, LongToIntFunction, DoubleToIntFunction, DoubleToLongFunction
         * 〇二つの引数を受け取る Rは戻り型
         *      BiFunction<T,U,R>, ToIntBiFunction<T,U>, ToLongBiFunction<T,U>, ToDoubleBiFunction<T,U>
         * 〇受け取りと戻り値の型が同じとき
         *      UnaryOperator<T>
         * 〇二つの引数を受け取り同じ型を返す <T,T,T>
         *      BinaryOperator<T>, IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator
         */

        // iterateはUnaryOperatorを第二引数にとり,第一引数を初期値として繰り返したStreamを返す
        Stream.iterate(1, x -> x * 10)
            .limit(5)
            .forEach(System.out::println);
    }
}

/**
 * Functionパッケージはこうなっている
 * FunctionalInterfaceは1メソッドのインターフェースを厳密に判定
 */
@FunctionalInterface
interface Func {
    int calc(int x, int y);
}