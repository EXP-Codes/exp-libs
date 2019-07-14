package exp.libs.utils.other;

import java.util.List;
import java.util.Random;

/**
 * <PRE>
 * 随机生成器工具.
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-02-02
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class RandomUtils {
	
	/** 随机对象 */
	private final static Random gen = new Random();
	
	/** 常用的1000个汉字集 */
	private final static char[] KANJI = new char[] {
		'一', '是', '了', '我', '不', '人', '在', '他', '有', '这', '个', '上', '们', '来', '到', 
		'时', '大', '地', '为', '子', '中', '你', '说', '生', '国', '年', '着', '就', '那', '和', 
		'要', '她', '出', '也', '得', '里', '后', '自', '以', '会', '家', '可', '下', '而', '过', 
		'天', '去', '能', '对', '小', '多', '然', '于', '心', '学', '么', '之', '都', '好', '看', 
		'起', '发', '当', '没', '成', '只', '如', '事', '把', '还', '用', '第', '样', '道', '想', 
		'作', '种', '开', '美', '总', '从', '无', '情', '己', '面', '最', '女', '但', '现', '前', 
		'些', '所', '同', '日', '手', '又', '行', '意', '动', '方', '期', '它', '头', '经', '长', 
		'儿', '回', '位', '分', '爱', '老', '因', '很', '给', '名', '法', '间', '斯', '知', '世', 
		'什', '两', '次', '使', '身', '者', '被', '高', '已', '亲', '其', '进', '此', '话', '常', 
		'与', '活', '正', '感', '见', '明', '问', '力', '理', '尔', '点', '文', '几', '定', '本', 
		'公', '特', '做', '外', '孩', '相', '西', '果', '走', '将', '月', '十', '实', '向', '声', 
		'车', '全', '信', '重', '三', '机', '工', '物', '气', '每', '并', '别', '真', '打', '太', 
		'新', '比', '才', '便', '夫', '再', '书', '部', '水', '像', '眼', '等', '体', '却', '加', 
		'电', '主', '界', '门', '利', '海', '受', '听', '表', '德', '少', '克', '代', '员', '许', 
		'稜', '先', '口', '由', '死', '安', '写', '性', '马', '光', '白', '或', '住', '难', '望', 
		'教', '命', '花', '结', '乐', '色', '更', '拉', '东', '神', '记', '处', '让', '母', '父', 
		'应', '直', '字', '场', '平', '报', '友', '关', '放', '至', '张', '认', '接', '告', '入', 
		'笑', '内', '英', '军', '候', '民', '岁', '往', '何', '度', '山', '觉', '路', '带', '万', 
		'男', '边', '风', '解', '叫', '任', '金', '快', '原', '吃', '妈', '变', '通', '师', '立', 
		'象', '数', '四', '失', '满', '战', '远', '格', '士', '音', '轻', '目', '条', '呢', '病', 
		'始', '达', '深', '完', '今', '提', '求', '清', '王', '化', '空', '业', '思', '切', '怎', 
		'非', '找', '片', '罗', '钱', '紶', '吗', '语', '元', '喜', '曾', '离', '飞', '科', '言', 
		'干', '流', '欢', '约', '各', '即', '指', '合', '反', '题', '必', '该', '论', '交', '终', 
		'林', '请', '医', '晚', '制', '球', '决', '窢', '传', '画', '保', '读', '运', '及', '则', 
		'房', '早', '院', '量', '苦', '火', '布', '品', '近', '坐', '产', '答', '星', '精', '视', 
		'五', '连', '司', '巴', '奇', '管', '类', '未', '朋', '且', '婚', '台', '夜', '青', '北', 
		'队', '久', '乎', '越', '观', '落', '尽', '形', '影', '红', '爸', '百', '令', '周', '吧', 
		'识', '步', '希', '亚', '术', '留', '市', '半', '热', '送', '兴', '造', '谈', '容', '极', 
		'随', '演', '收', '首', '根', '讲', '整', '式', '取', '照', '办', '强', '石', '古', '华', 
		'諣', '拿', '计', '您', '装', '似', '足', '双', '妻', '尼', '转', '诉', '米', '称', '丽', 
		'客', '南', '领', '节', '衣', '站', '黑', '刻', '统', '断', '福', '城', '故', '历', '惊', 
		'脸', '选', '包', '紧', '争', '另', '建', '维', '绝', '树', '系', '伤', '示', '愿', '持', 
		'千', '史', '谁', '准', '联', '妇', '纪', '基', '买', '志', '静', '阿', '诗', '独', '复', 
		'痛', '消', '社', '算', '义', '竟', '确', '酒', '需', '单', '治', '卡', '幸', '兰', '念', 
		'举', '仅', '钟', '怕', '共', '毛', '句', '息', '功', '官', '待', '究', '跟', '穿', '室', 
		'易', '游', '程', '号', '居', '考', '突', '皮', '哪', '费', '倒', '价', '图', '具', '刚', 
		'脑', '永', '歌', '响', '商', '礼', '细', '专', '黄', '块', '脚', '味', '灵', '改', '据', 
		'般', '破', '引', '食', '仍', '存', '众', '注', '笔', '甚', '某', '沉', '血', '备', '习', 
		'校', '默', '务', '土', '微', '娘', '须', '试', '怀', '料', '调', '广', '蜖', '苏', '显', 
		'赛', '查', '密', '议', '底', '列', '富', '梦', '错', '座', '参', '八', '除', '跑', '亮', 
		'假', '印', '设', '线', '温', '虽', '掉', '京', '初', '养', '香', '停', '际', '致', '阳', 
		'纸', '李', '纳', '验', '助', '激', '够', '严', '证', '帝', '饭', '忘', '趣', '支', '春', 
		'集', '丈', '木', '研', '班', '普', '导', '顿', '睡', '展', '跳', '获', '艺', '六', '波', 
		'察', '群', '皇', '段', '急', '庭', '创', '区', '奥', '器', '谢', '弟', '店', '否', '害', 
		'草', '排', '背', '止', '组', '州', '朝', '封', '睛', '板', '角', '况', '曲', '馆', '育', 
		'忙', '质', '河', '续', '哥', '呼', '若', '推', '境', '遇', '雨', '标', '姐', '充', '围', 
		'案', '伦', '护', '冷', '警', '贝', '著', '雪', '索', '剧', '啊', '船', '险', '烟', '依', 
		'斗', '值', '帮', '汉', '慢', '佛', '肯', '闻', '唱', '沙', '局', '伯', '族', '低', '玩', 
		'资', '屋', '击', '速', '顾', '泪', '洲', '团', '圣', '旁', '堂', '兵', '七', '露', '园', 
		'牛', '哭', '旅', '街', '劳', '型', '烈', '姑', '陈', '莫', '鱼', '异', '抱', '宝', '权', 
		'鲁', '简', '态', '级', '票', '怪', '寻', '杀', '律', '胜', '份', '汽', '右', '洋', '范', 
		'床', '舞', '秘', '午', '登', '楼', '贵', '吸', '责', '例', '追', '较', '职', '属', '渐', 
		'左', '录', '丝', '牙', '党', '继', '托', '赶', '章', '智', '冲', '叶', '胡', '吉', '卖', 
		'坚', '喝', '肉', '遗', '救', '修', '松', '临', '藏', '担', '戏', '善', '卫', '药', '悲', 
		'敢', '靠', '伊', '村', '戴', '词', '森', '耳', '差', '短', '祖', '云', '规', '窗', '散', 
		'迷', '油', '旧', '适', '乡', '架', '恩', '投', '弹', '铁', '博', '雷', '府', '压', '超', 
		'负', '勒', '杂', '醒', '洗', '采', '毫', '嘴', '毕', '九', '冰', '既', '状', '乱', '景', 
		'席', '珍', '童', '顶', '派', '素', '脱', '农', '疑', '练', '野', '按', '犯', '拍', '征', 
		'坏', '骨', '余', '承', '置', '臓', '彩', '灯', '巨', '琴', '免', '环', '姆', '暗', '换', 
		'技', '翻', '束', '增', '忍', '餐', '洛', '塞', '缺', '忆', '判', '欧', '层', '付', '阵', 
		'玛', '批', '岛', '项', '狗', '休', '懂', '武', '革', '良', '恶', '恋', '委', '拥', '娜', 
		'妙', '探', '呀', '营', '退', '摇', '弄', '桌', '熟', '诺', '宣', '银', '势', '奖', '宫', 
		'忽', '套', '康', '供', '优', '课', '鸟', '喊', '降', '夏', '困', '刘', '罪', '亡', '鞋', 
		'健', '模', '败', '伴', '守', '挥', '鲜', '财', '孤', '枪', '禁', '恐', '伙', '杰', '迹', 
		'妹', '藸', '遍', '盖', '副', '坦', '牌', '江', '顺', '秋', '萨', '菜', '划', '授', '归', 
		'浪', '听', '凡', '预', '奶', '雄', '升', '碃', '编', '典', '袋', '莱', '含', '盛', '济', 
		'蒙', '棋', '端', '腿', '招', '释', '介', '烧', '误'
	};
	
	/** 单姓集  */
	private final static String[][] SINGLE_SURNAME = {
		new String[] {
				"bai", "bai", 
				"cai", "cao", "chen", 
				"dai", "dou", "deng", "di", "du", "duan", 
				"fan", "fan", "fang", "feng", "fu", "fu", 
				"gao", "gu", "guan", "guo", 
				"han", "hu", "hua", "hong", "hou", "huang", 
				"jia", "jiang", "jin", 
				"liao", "liang", "li", "lin", "liu", "long", "lu", "lu", "luo", 
				"ma", "mao", 
				"niu", 
				"ou", "ou", 
				"pang", "pei", "peng", 
				"qi", "qi", "qian", "qiao", "qin", "qiu", "qiu", "qiu", 
				"sha", "shang", "shang", "shao", "shen", "shi", "shi", "song", "sun", 
				"tong", 
				"wan", "wang", "wei", "wei", "wu", "wu", 
				"xiao", "xiao", "xiang", "xu", "xu", "xue", 
				"yang", "yang", "yang", "yi", "yin", "yu", 
				"zhao", "zhong", "zhou", "zheng", "zhu"
		}, 
		new String[] {
				"百", "白", 
				"蔡", "曹", "陈", 
				"戴", "窦", "邓", "狄", "杜", "段", 
				"范", "樊", "房", "风", "符", "福", 
				"高", "古", "关", "郭", 
				"韩", "胡", "花", "洪", "侯", "黄", 
				"贾", "蒋", "金", 
				"廖", "梁", "李", "林", "刘", "龙", "陆", "卢", "罗", 
				"马", "毛", 
				"牛", 
				"欧", "区", 
				"庞", "裴", "彭", 
				"戚", "齐", "钱", "乔", "秦", "邱", "裘", "仇", 
				"沙", "商", "尚", "邵", "沈", "师", "施", "宋", "孙", 
				"童", 
				"万", "王", "魏", "卫", "吴", "武", 
				"萧", "肖", "项", "许", "徐", "薛", 
				"杨", "羊", "阳", "易", "尹", "俞", 
				"赵", "钟", "周", "郑", "朱"
		},
	};
	
	/** 复姓集  */
	private final static String[][] COMPOUND_SURNAME = {
		new String[] {
				"dongfang", "dugu", "murong", "ouyang", 
				"sima", "ximen", "yuchi", "zhangsun", "zhuge"
		}, 
		new String[] {
				"东方", "独孤", "慕容", "欧阳", 
				"司马", "西门", "尉迟", "长孙", "诸葛", 
		},
	};
	
	/** 名字集  */
	public final static String[][] NAME = {
		new String[] {
				"ai", "an", "ao", "ang", 
				"ba", "bai", "ban", "bang", "bei", "biao", "bian", "bu", 
				"cao", "cang", "chang", "chi", "ci", 
				"du", "dong", "dou", 
				"fa", "fan", "fang", "feng", "fu", 
				"gao", 
				"hong", "hu", "hua", "hao", 
				"ji", "jian", 
				"kan", "ke", 
				"lang", "li", "lin", 
				"ma", "mao", "miao", 
				"nan", "niu", 
				"pian", 
				"qian", "qiang", "qin", "qing", 
				"ran", "ren", 
				"sha", "shang", "shen", "shi", "shui", "si", "song", 
				"tang", "tong", "tian", 
				"wan", "wei", "wu", 
				"xi", "xiao", "xiong", 
				"yang", "yi", "yin", "ying", "you", "yu", 
				"zhi", "zhong", "zhou", "zhu", "zhuo", "zi", "zong", "zu", "zuo"
		},
		new String[] {
				"皑艾哀", "安黯谙", "奥傲敖骜翱", "昂盎", 
				"罢霸", "白佰", "斑般", "邦", "北倍贝备", "表标彪飚飙", "边卞弁忭", "步不", 
				"曹草操漕", "苍仓", "常长昌敞玚", "迟持池赤尺驰炽", "此次词茨辞慈", 
				"独都", "东侗", "都", 
				"发乏珐", "范凡反泛帆蕃", "方访邡昉", "风凤封丰奉枫峰锋", "夫符弗芙", 
				"高皋郜镐", 
				"洪红宏鸿虹泓弘", "虎忽湖护乎祜浒怙", "化花华骅桦", "号浩皓蒿浩昊灏淏", 
				"积极济技击疾及基集记纪季继吉计冀祭际籍绩忌寂霁稷玑芨蓟戢佶奇诘笈畿犄", "渐剑见建间柬坚俭", 
				"刊戡", "可克科刻珂恪溘牁", 
				"朗浪廊琅阆莨", "历离里理利立力丽礼黎栗荔沥栎璃", "临霖林琳", 
				"马", "贸冒貌冒懋矛卯瑁", "淼渺邈", 
				"楠南", "牛妞", 
				"片翩", 
				"潜谦倩茜乾虔千", "强羌锖玱", "亲琴钦沁芩矜", "清庆卿晴", 
				"冉然染燃", "仁刃壬仞", 
				"沙煞", "上裳商", "深审神申慎参莘", "师史石时十世士诗始示适炻", "水", "思斯丝司祀嗣巳", "松颂诵", 
				"堂唐棠瑭", "统通同童彤仝", "天田忝", 
				"万宛晚", "卫微伟维威韦纬炜惟玮为", "吴物务武午五巫邬兀毋戊", 
				"西席锡洗夕兮熹惜", "潇萧笑晓肖霄骁校", "熊雄", 
				"羊洋阳漾央秧炀飏鸯", "易意依亦伊夷倚毅义宜仪艺译翼逸忆怡熠沂颐奕弈懿翊轶屹猗翌", "隐因引银音寅吟胤訚烟荫", "映英影颖瑛应莹郢鹰", "幽悠右忧猷酉", "渔郁寓于余玉雨语预羽舆育宇禹域誉瑜屿御渝毓虞禺豫裕钰煜聿", 
				"制至值知质致智志直治执止置芝旨峙芷挚郅炙雉帜", "中忠钟衷", "周州舟胄繇昼", "竹主驻足朱祝诸珠著竺", "卓灼灼拙琢濯斫擢焯酌", "子资兹紫姿孜梓秭", "宗枞", "足族祖卒", "作左佐笮凿"
		}
	};
	
	/** 单字-单姓: 30% */
	private final static int SINGLE_SURNAME_1WORD = 30;
	
	/** 单字-双姓: 5% */
	private final static int TWOSYLLABLE_SURNAME_1WORD = 35;
	
	/** 单字-复姓: 5% */
	private final static int COMPOUND_SURNAME_1WORD = 40;
	
	/** 二字-单姓: 50% */
	private final static int SINGLE_SURNAME_2WORD = 90;
	
	/** 二字-双姓: 5% */
	private final static int TWOSYLLABLE_SURNAME_2WORD = 95;
	
	/** 二字-复姓: 5% */
	private final static int COMPOUND_SURNAME_2WORD = 100;
	
	/** 私有化构造函数 */
	protected RandomUtils() {}
	
	/**
	 * 获取int随机数
	 * @return 返回随机数范围 [0,+∞)
	 */
	public static int genInt() {
		return gen.nextInt();
	}
	
	/**
	 * 获取int随机数
	 * @param scope 随机数限界
	 * @return 返回随机数范围 [0,scope)
	 */
	public static int genInt(int scope) {
		return (scope <= 0 ? 0 : gen.nextInt(scope));
	}
	
	/**
	 * 获取int随机数
	 * @param min 随机数限界最小值
	 * @param max 随机数限界最大值
	 * @return 返回随机数范围 [min,max]
	 */
	public static int genInt(final int min, final int max) {
		int num = genInt(max - min + 1);
		return num + min;
	}
	
	/**
	 * 获取long随机数
	 * @return 返回随机数范围 [0,+∞)
	 */
	public static long genLong() {
		return gen.nextLong();
	}
	
	/**
	 * 获取float随机数
	 * @return 返回随机数范围 [0,+∞)
	 */
	public static float genFloat() {
		return gen.nextFloat();
	}
	
	/**
	 * 获取double随机数
	 * @return 返回随机数范围 [0,+∞)
	 */
	public static double genDouble() {
		return gen.nextDouble();
	}
	
	/**
	 * 获取bool随机数
	 * @return 返回随机数范围 true|false
	 */
	public static boolean genBoolean() {
		return gen.nextBoolean();
	}
	
	/**
	 * 随机获取队列中的某个元素
	 * @param list 队列（建议使用ArrayList）
	 * @return 队列中的元素, 若队列为空, 则返回null
	 */
	public static <E> E genElement(List<E> list) {
		E e = null;
		if(ListUtils.isNotEmpty(list)) {
			int idx = genInt(list.size());
			e = list.get(idx);
		}
		return e;
	}
	
	/**
	 * 随机获取集合中的某个元素
	 * @param array 队列（建议使用ArrayList）
	 * @return 队列中的元素, 若队列为空, 则返回null
	 */
	@SuppressWarnings("unchecked")
	public static <E> E genElement(E... array) {
		E e = null;
		if(ListUtils.isNotEmpty(array)) {
			int idx = genInt(array.length);
			e = array[idx];
		}
		return e;
	}
	
	/**
	 * 获取高斯随机数
	 * @return 返回随机数范围 (-∞,+∞)
	 */
	public static double genGaussian() {
		return gen.nextGaussian();
	}
	
	/**
	 * 随机生成 + - * / 之间的一个字符
	 * @return 随机四则运算符
	 */
	public static char genOperator() {
		char[] ops = new char[] { '+', '-', '*', '/' };
		int idx = genInt(ops.length);
		return ops[idx];
	}
	
	/**
	 * 随机生成0-9之间的一个数字
	 * @return 随机数字
	 */
	public static int genDigit() {
		return genInt(10);
	}
	
	/**
	 * 随机生成 a-z 小写字母
	 * @return 随机小写字母
	 */
	public static char genLowercase() {
		return (char) genInt(97, 122);
	}
	
	/**
	 * 随机生成 A-Z 大写字母
	 * @return 随机大写字母
	 */
	public static char genUppercase() {
		return (char) genInt(65, 90);
	}
	
	/**
	 * 随机生成一个 0-9A-Za-z 内的字符
	 * @return 返回随机单词字符
	 */
	public static char genWordChar() {
		int digit = genInt(48, 57);	// 0-9
		int upper = genInt(65, 90);	// A-Z
		int lower = genInt(97, 122);	// a-z
		int[] scope = new int[] { digit, upper, lower };
		int idx = genInt(scope.length);
		return (char) scope[idx];
	}
	
	/**
	 * 随机生成一个汉字(约20000个汉字中的一个)
	 * @return 随机汉字
	 */
	public static char genAllKanji() {
		int min = 0x4E00;
		int max = 0x9FA5;
		int random = genInt(min, max);	// unicode的汉字范围为4E00-9FA5
        return (char) random;
    }
	
	/**
	 * 随机生成常用的1000个汉字中的一个
	 * @return 随机常用汉字
	 */
	public static char genUsedKanji() {
		int idx = genInt(KANJI.length);
        return KANJI[idx];
    }
	
	/**
	 * 获取随机姓名（拼音）
	 * @return 随机姓名（拼音）
	 */
	public static String genSpellName() {
		return genChineseName()[0];
	}
	
	/**
	 * 获取随机姓名（汉字）
	 * @return 随机姓名（汉字）
	 */
	public static String genKanjiName() {
		return genChineseName()[1];
	}
	
	/**
	 * 获取随机姓名
	 * @return 返回值为只有2个元素的数组，其中 0:拼音名; 1:中文名
	 */
	public static String[] genChineseName() {
		String[] chineseName = new String[2];
		
		String[] compoundSurname = genCompoundSurname();
		String[] singleSurname1 = genSingleSurname();
		String[] singleSurname2 = genSingleSurname();
		String[] word1 = genName();
		String[] word2 = genName();
		
		int nameType = genInt(1, 100);
		if(nameType <= SINGLE_SURNAME_1WORD) {
			chineseName[0] = StrUtils.concat(singleSurname1[0], "-", word1[0]);
			chineseName[1] = StrUtils.concat(singleSurname1[1], word1[1]);
			
		} else if(nameType <= TWOSYLLABLE_SURNAME_1WORD) {
			chineseName[0] = StrUtils.concat(singleSurname1[0], singleSurname2[0], "-", word1[0]);
			chineseName[1] = StrUtils.concat(singleSurname1[1], singleSurname2[1], word1[1]);
			
		} else if(nameType <= COMPOUND_SURNAME_1WORD) {
			chineseName[0] = StrUtils.concat(compoundSurname[0], "-", word1[0]);
			chineseName[1] = StrUtils.concat(compoundSurname[1], word1[1]);
			
		} else if(nameType <= SINGLE_SURNAME_2WORD) {
			chineseName[0] = StrUtils.concat(singleSurname1[0], "-", word1[0], word2[0]);
			chineseName[1] = StrUtils.concat(singleSurname1[1], word1[1], word2[1]);
			
		} else if(nameType <= TWOSYLLABLE_SURNAME_2WORD) {
			chineseName[0] = StrUtils.concat(singleSurname1[0], singleSurname2[0], "-", word1[0], word2[0]);
			chineseName[1] = StrUtils.concat(singleSurname1[1], singleSurname2[1], word1[1], word2[1]);
			
		} else if(nameType <= COMPOUND_SURNAME_2WORD) {
			chineseName[0] = StrUtils.concat(compoundSurname[0], "-", word1[0], word2[0]);
			chineseName[1] = StrUtils.concat(compoundSurname[1], word1[1], word2[1]);
		}
		
		return chineseName;
	}
	
	/**
	 * 随机生成单姓
	 * @return
	 */
	private static String[] genSingleSurname() {
		int idx = genInt(SINGLE_SURNAME[0].length);
		return new String[] { SINGLE_SURNAME[0][idx], SINGLE_SURNAME[1][idx] };
	}
	
	/**
	 * 随机生成复姓
	 * @return
	 */
	private static String[] genCompoundSurname() {
		int idx = genInt(COMPOUND_SURNAME[0].length);
		return new String[] { COMPOUND_SURNAME[0][idx], COMPOUND_SURNAME[1][idx] };
	}
	
	/**
	 * 随机生成名字
	 * @return
	 */
	private static String[] genName() {
		int idx = genInt(NAME[0].length);
		String[] word = { NAME[0][idx], NAME[1][idx] };
		
		char[] kanji = word[1].toCharArray();
		word[1] = String.valueOf(kanji[genInt(kanji.length)]);
		return word;
	}
	
}

