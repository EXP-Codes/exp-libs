package exp.libs.algorithm.basic;

import java.util.BitSet;

/**
 * FIXME 布隆过滤器
 */
public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 24;//布隆过滤器的比特长度
    private static final int[] seeds = {3,5,7, 11, 13, 31, 37, 61};//这里要选取质数，能很好的降低错误率
    private static BitSet bits = new BitSet(DEFAULT_SIZE);
    private static SimpleHash[] func = new SimpleHash[seeds.length];

    public static void addValue(String value)
    {
        for(SimpleHash f : func)//将字符串value哈希为8个或多个整数，然后在这些整数的bit上变为1
            bits.set(f.hash(value),true);
    }
    
    public static void add(String value)
    {
        if(value != null) addValue(value);
    }
    
    public static boolean contains(String value)
    {
        if(value == null) return false;
        boolean ret = true;
        for(SimpleHash f : func)//这里其实没必要全部跑完，只要一次ret==false那么就不包含这个字符串
            ret = ret && bits.get(f.hash(value));
        return ret;
    }
    
    public static void main(String[] args) {
        String value = "xkeyideal@gmail.com";
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
        add(value);
        System.out.println(contains(value));
    }
}

class SimpleHash {//这玩意相当于C++中的结构体

    private int cap;
    private int seed;

    public  SimpleHash(int cap, int seed) {
        this.cap = cap;
        this.seed = seed;
    }

    public int hash(String value) {//字符串哈希，选取好的哈希函数很重要
        int result = 0;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            result = seed * result + value.charAt(i);
        }
        return (cap - 1) & result;
    }
}
