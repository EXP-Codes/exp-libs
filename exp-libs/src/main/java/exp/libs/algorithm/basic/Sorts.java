package exp.libs.algorithm.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorts {

	public static void radixSort() {
		// TODO 基排
	}
	
	public static void quickSort() {
		// TODO 快排
	}
	
	public static void heapSort() {
		// TODO: 堆排
	}
	
	public static void binSort() {
		// TODO: 桶排序/箱排序
	}
	
	/**
	 * <pre>
	 * 冒泡排序
	 * 在最优情况下只需要经过n-1次比较即可得出结果(这个最优情况那就是序列己是正序,
	 * 从100K的正序结果可以看出结果正是如此),但在最坏情况下，即倒序（或一个较小值在最后）,
	 * 下沉算法将需要n(n-1)/2次比较。所以一般情况下,特别是在逆序时,它很不理想。
	 * 它是对数据有序性非常敏感的排序算法。
	 * 默认为升序
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 */
	@SuppressWarnings("rawtypes")
	public static void bubbleSort(List<Comparable> list) {
		bubbleSort(list, true);
	}
	
	/**
	 * <pre>
	 * 冒泡排序
	 * 在最优情况下只需要经过n-1次比较即可得出结果(这个最优情况那就是序列己是正序,
	 * 从100K的正序结果可以看出结果正是如此),但在最坏情况下，即倒序（或一个较小值在最后）,
	 * 下沉算法将需要n(n-1)/2次比较。所以一般情况下,特别是在逆序时,它很不理想。
	 * 它是对数据有序性非常敏感的排序算法。
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 * @param asc
	 *            是否升序排列，true升序，false降序
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void bubbleSort(List<Comparable> list, boolean asc) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - i - 1; j++) {
				Comparable a;
				if (asc && list.get(j).compareTo(list.get(j + 1)) > 0) { // 升序
					a = list.get(j);
					list.set((j), list.get(j + 1));
					list.set(j + 1, a);
				} else if (!asc & list.get(j).compareTo(list.get(j + 1)) < 0) { // 降序
					a = list.get(j);
					list.set((j), list.get(j + 1));
					list.set(j + 1, a);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 选择排序
	 * 它的比较次数一定：n(n-1)/2。也因此无论在序列何种情况下，
	 * 它都不会有优秀的表现（从上100K的正序和反序数据可以发现它耗时相差不多，
	 * 相差的只是数据移动时间），可见对数据的有序性不敏感。它虽然比较次数多，
	 * 但它的数据交换量却很少。所以我们将发现它在一般情况下将快于冒泡排序。
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 * @param asc
	 *            是否升序排列
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void selectionSort(List<Comparable> list, boolean asc) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (asc && (list.get(i).compareTo(list.get(j)) > 0)) {
					Comparable temp = list.get(i);
					list.set((i), list.get(j));
					list.set(j, temp);
				} else if (!asc && list.get(i).compareTo(list.get(j)) < 0) {
					Comparable temp = list.get(i);
					list.set((i), list.get(j));
					list.set(j, temp);
				}
			}
		}
	}

	/**
	 * <pre>
	 * 选择排序
	 * 它的比较次数一定：n(n-1)/2。也因此无论在序列何种情况下，
	 * 它都不会有优秀的表现（从上100K的正序和反序数据可以发现它耗时相差不多，
	 * 相差的只是数据移动时间），可见对数据的有序性不敏感。它虽然比较次数多，
	 * 但它的数据交换量却很少。所以我们将发现它在一般情况下将快于冒泡排序。
	 * 默认是升序
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 */
	@SuppressWarnings("rawtypes")
	public static void selectionSort(List<Comparable> list) {
		selectionSort(list, true);
	}

	/**
	 * <pre>
	 * 插入排序
	 * 每次比较后最多移掉一个逆序，因此与冒泡排序的效率相同.但它在速度
	 * 上还是要高点，这是因为在冒泡排序下是进行值交换，而在插入排序下是值移动,
	 * 所以直接插入排序将要优于冒泡排序.直接插入法也是一种对数据的有序性非常敏感的一种算法.
	 * 在有序情况下只需要经过n-1次比较,在最坏情况下,将需要n(n-1)/2次比较
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 * @param asc
	 *            是否升序排列
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertSort(List<Comparable> list, boolean asc) {
		int i;
		int j;
		Comparable temp;
		for (i = 1; i < list.size(); i++) {
			temp = list.get(i);
			if (asc) {
				for (j = i - 1; j >= 0 && temp.compareTo(list.get(j)) < 0; j--) {
					list.set((j + 1), list.get(j));
				}
				list.set((j + 1), temp);
			} else {
				for (j = i - 1; j >= 0 && temp.compareTo(list.get(j)) > 0; j--) {
					list.set((j + 1), list.get(j));
				}
				list.set((j + 1), temp);
			}
		}
	}

	/**
	 * <pre>
	 * 插入排序
	 * 每次比较后最多移掉一个逆序，因此与冒泡排序的效率相同.但它在速度
	 * 上还是要高点，这是因为在冒泡排序下是进行值交换，而在插入排序下是值移动,
	 * 所以直接插入排序将要优于冒泡排序.直接插入法也是一种对数据的有序性非常敏感的一种算法.
	 * 在有序情况下只需要经过n-1次比较,在最坏情况下,将需要n(n-1)/2次比较
	 * 默认为升序
	 * </pre>
	 * 
	 * @param list
	 *            集合
	 */
	@SuppressWarnings("rawtypes")
	public static void insertSort(List<Comparable> list) {
		insertSort(list, true);
	}

	/**
	 * <pre>
	 * 快速排序
	 * 它同样是冒泡排序的改进，它通过一次交换能消除多个逆序，这样可以减少逆序时所消耗的扫描和数据交换次数.
	 * 在最优情况下，它的排序时间复杂度为O(nlog2n).即每次划分序列时，能均匀分成两个子串.
	 * 但最差情况下它的时间复杂度将是O(n&circ;2).即每次划分子串时，一串为空，另一串为m-1
	 * (程序中的100K正序和逆序就正是这样，如果程序中采用每次取序列中部数据作为划分点，那将在正序和逆时达到最优).
	 * 从100K中正序的结果上看“快速排序”会比“冒泡排序”更慢，这主要是“冒泡排序”中采用了提前结束排序的方法.
	 * 有的书上这解释“快速排序”，在理论上讲，如果每次能均匀划分序列，它将是最快的排序算法，因此称它作快速排序.
	 * 虽然很难均匀划分序列，但就平均性能而言，它仍是基于关键字比较的内部排序算法中速度最快者。 
	 * 默认为升序
	 * </pre>
	 * 
	 * @param obj
	 *            集合
	 */
	@SuppressWarnings("rawtypes")
	public static void quickSort(List<Comparable> obj) {
		quickSort(obj, 0, obj.size(), true);
	}

	/**
	 * <pre>
	 * 快速排序
	 * 它同样是冒泡排序的改进，它通过一次交换能消除多个逆序，这样可以减少逆序时所消耗的扫描和数据交换次数.
	 * 在最优情况下，它的排序时间复杂度为O(nlog2n).即每次划分序列时，能均匀分成两个子串.
	 * 但最差情况下它的时间复杂度将是O(n&circ;2).即每次划分子串时，一串为空，另一串为m-1
	 * (程序中的100K正序和逆序就正是这样，如果程序中采用每次取序列中部数据作为划分点，那将在正序和逆时达到最优).
	 * 从100K中正序的结果上看“快速排序”会比“冒泡排序”更慢，这主要是“冒泡排序”中采用了提前结束排序的方法.
	 * 有的书上这解释“快速排序”，在理论上讲，如果每次能均匀划分序列，它将是最快的排序算法，因此称它作快速排序.
	 * 虽然很难均匀划分序列，但就平均性能而言，它仍是基于关键字比较的内部排序算法中速度最快者。
	 * </pre>
	 * 
	 * @param obj
	 *            集合
	 * @param asc
	 *            是否升序排列
	 */
	@SuppressWarnings("rawtypes")
	public static void quickSort(List<Comparable> obj, boolean asc) {
		quickSort(obj, 0, obj.size(), asc);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void quickSort(List<Comparable> obj, int left, int right,
			boolean asc) {
		int i, j;
		Comparable middle, temp;
		i = left;
		j = right;
		middle = obj.get(left);
		while (true) {
			if (asc) {
				while ((++i) < right - 1 && obj.get(i).compareTo(middle) < 0)
					;
				while ((--j) > left && obj.get(j).compareTo(middle) > 0)
					;
				if (i >= j)
					break;
				temp = obj.get(i);
				obj.set(i, obj.get(j));
				obj.set(j, temp);
			} else {
				while ((++i) < right - 1 && obj.get(i).compareTo(middle) > 0)
					;
				while ((--j) > left && obj.get(j).compareTo(middle) < 0)
					;
				if (i >= j)
					break;
				temp = obj.get(i);
				obj.set(i, obj.get(j));
				obj.set(j, temp);
			}
		}
		obj.set(left, obj.get(j));
		obj.set(j, middle);

		if (left < j)
			quickSort(obj, left, j, asc);

		if (right > i)
			quickSort(obj, i, right, asc);
	}

	/**
	 * <pre>
	 * 归并排序
	 * 归并排序是一种非就地排序,将需要与待排序序列一样多的辅助空间.在使用它对两个己有序的序列归并,
	 * 将有无比的优势.其时间复杂度无论是在最好情况下还是在最坏情况下均是O(nlog2n).
	 * 对数据的有序性不敏感.若数据节点数据量大,那将不适合.但可改造成索引操作,效果将非常出色. 
	 * 默认为升序
	 * </pre>
	 * 
	 * @param obj
	 *            集合
	 */
	@SuppressWarnings("rawtypes")
	public static void mergeSort(List<Comparable> obj) {
		List<Comparable> bridge = new ArrayList<Comparable>(obj); // 初始化中间数组
		mergeSort(obj, 0, obj.size() - 1, true, bridge); // 归并排序
		bridge = null;
	}

	/**
	 * <pre>
	 * 归并排序
	 * 归并排序是一种非就地排序,将需要与待排序序列一样多的辅助空间.在使用它对两个己有序的序列归并,
	 * 将有无比的优势.其时间复杂度无论是在最好情况下还是在最坏情况下均是O(nlog2n).
	 * 对数据的有序性不敏感.若数据节点数据量大,那将不适合.但可改造成索引操作,效果将非常出色.
	 * </pre>
	 * 
	 * @param obj
	 *            集合
	 * @param asc
	 *            是否升序排列
	 */
	@SuppressWarnings("rawtypes")
	public static void mergeSort(List<Comparable> obj, boolean asc) {
		List<Comparable> bridge = new ArrayList<Comparable>(obj); // 初始化中间数组
		mergeSort(obj, 0, obj.size() - 1, asc, bridge); // 归并排序
		bridge = null;
	}

	@SuppressWarnings("rawtypes")
	private static void mergeSort(List<Comparable> obj, int left, int right,
			boolean asc, List<Comparable> bridge) {
		if (left < right) {
			int center = (left + right) / 2;
			mergeSort(obj, left, center, asc, bridge);
			mergeSort(obj, center + 1, right, asc, bridge);
			merge(obj, left, center, right, asc, bridge);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void merge(List<Comparable> obj, int left, int center,
			int right, boolean asc, List<Comparable> bridge) {
		int mid = center + 1;
		int third = left;
		int tmp = left;
		while (left <= center && mid <= right) { // 从两个数组中取出小的放入中间数组
			if (asc) {
				if (obj.get(left).compareTo(obj.get(mid)) <= 0) {
					bridge.set(third++, obj.get(left++));
				} else {
					bridge.set(third++, obj.get(mid++));
				}
			} else {
				if (obj.get(left).compareTo(obj.get(mid)) > 0) {
					bridge.set(third++, obj.get(left++));
				} else {
					bridge.set(third++, obj.get(mid++));
				}
			}
		}

		// 剩余部分依次置入中间数组
		while (mid <= right) {
			bridge.set(third++, obj.get(mid++));
		}
		while (left <= center) {
			bridge.set(third++, obj.get(left++));
		}
		// 将中间数组的内容拷贝回原数组
		copy(obj, tmp, right, bridge);
	}

	@SuppressWarnings("rawtypes")
	private static void copy(List<Comparable> obj, int left, int right,
			List<Comparable> bridge) {
		while (left <= right) {
			obj.set(left, bridge.get(left));
			left++;
		}
	}

	/**
	 * <pre>
	 * 对排序后的队列进行快速合并，使合并后的队列有序(归并排序算法).
	 * 默认为升序
	 * 
	 * 使用实例：
	 *  List list1 = new ArrayList();
	 * list1.add(&quot;dsd&quot;);
	 * list1.add(&quot;tsest&quot;);
	 * list1.add(&quot;ga&quot;);
	 * list1.add(&quot;aaaa&quot;);
	 * list1.add(&quot;bb&quot;);
	 * ListUtils.quickSort(list1);
	 * 
	 * List list2 = new ArrayList();
	 * list2.add(&quot;cc&quot;);
	 * list2.add(&quot;dd&quot;);
	 * list2.add(&quot;zz&quot;);
	 * ListUtils.quickSort(list2);
	 * 
	 * List listAll = new ArrayList();
	 * listAll.add(list1);
	 * listAll.add(list2);
	 * 
	 * List sortList = ListUtils.listMerge(listAll);
	 * System.out.println(sortList);
	 * </pre>
	 * 
	 * @param objs
	 *            集合的集合
	 * @return 排序后的集合
	 */
	@SuppressWarnings("rawtypes")
	public static List listMerge(List<List<Comparable>> objs) {
		return listMerge(objs, true);
	}

	/**
	 * <pre>
	 * 对排序后的队列进行快速合并，使合并后的队列有序(归并排序算法).
	 * List list1 = new ArrayList();
	 * list1.add(&quot;dsd&quot;);
	 * list1.add(&quot;tsest&quot;);
	 * list1.add(&quot;ga&quot;);
	 * list1.add(&quot;aaaa&quot;);
	 * list1.add(&quot;bb&quot;);
	 * ListUtils.quickSort(list1);
	 * 
	 * List list2 = new ArrayList();
	 * list2.add(&quot;cc&quot;);
	 * list2.add(&quot;dd&quot;);
	 * list2.add(&quot;zz&quot;);
	 * ListUtils.quickSort(list2);
	 * 
	 * List listAll = new ArrayList();
	 * listAll.add(list1);
	 * listAll.add(list2);
	 * 
	 * List sortList = ListUtils.listMerge(listAll,false);
	 * System.out.println(sortList);
	 * </pre>
	 * 
	 * @param objs
	 *            集合的集合
	 * @param asc
	 *            是否升序排列
	 * @return 排序后的集合
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List listMerge(List<List<Comparable>> objs, boolean asc) {
		List list = new ArrayList();
		for (List<Comparable> obj : objs) {
			list.addAll(obj);
			mergeSort(list, asc);
		}
		return list;
	}

	/**
	 * <pre>
	 * 二分查找法
	 * 在进行此调用之前，必须根据列表元素的自然顺序对列表进行升序排序(通过 sort(List) 方法).
	 * 如果没有对列表进行排序，则结果是不确定的。如果列表包含多个等于指定对象的元素，则无法保证找到的是哪一个。
	 * </pre>
	 * 
	 * @param obj
	 *            集合
	 * @param key
	 *            查找内容
	 * @return 关键字在集合中的位置
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int binarySearch(List obj, Object key) {
		int index = Collections.binarySearch(obj, key);
		return index;
	}

	/**
	 * 根据下标找到相对应的对象
	 * 
	 * @param obj
	 *            集合
	 * @param key
	 *            查找内容
	 * @return 对应的对象值
	 */
	@SuppressWarnings("rawtypes")
	public static Object getObject(List obj, int key) {
		return obj.get(key);
	}
}
