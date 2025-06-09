/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2025 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.candles;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.num.Num;
import java.util.ArrayList;
import java.util.List;

/**
 * 头肩底模式
 */
public class HeadAndShoulderBottomIndicator extends CachedIndicator<Boolean> {

    // 默认参数值
    private static final int DEFAULT_MIN_TROUGH_DISTANCE = 3; // 默认谷值最小间距
    private static final double DEFAULT_MIN_HEIGHT_RATIO = 0.12; // 默认最小高度比例12%

    // 使用的指标
    private final HighPriceIndicator highPriceIndicator; // 最高价指标
    private final LowPriceIndicator lowPriceIndicator;  // 最低价指标
    private final ClosePriceIndicator closePriceIndicator; // 收盘价指标

    // 配置参数
    private final int minTroughDistance; // 谷值间最小距离
    private final double minHeightRatio; // 肩部间最小高度比例

    /**
     * 使用默认参数的构造方法
     *
     * @param series 价格序列
     */
    public HeadAndShoulderBottomIndicator(BarSeries series) {
        this(series, DEFAULT_MIN_TROUGH_DISTANCE, DEFAULT_MIN_HEIGHT_RATIO);
    }

    /**
     * 自定义参数的构造方法
     *
     * @param series 价格序列
     * @param minTroughDistance 谷值间最小距离
     * @param minHeightRatio 肩部间最小高度比例
     */
    public HeadAndShoulderBottomIndicator(BarSeries series, int minTroughDistance, double minHeightRatio) {
        super(series);
        this.highPriceIndicator = new HighPriceIndicator(series);
        this.lowPriceIndicator = new LowPriceIndicator(series);
        this.closePriceIndicator = new ClosePriceIndicator(series);
        this.minTroughDistance = minTroughDistance;
        this.minHeightRatio = minHeightRatio;
    }

    @Override
    protected Boolean calculate(int index) {
        // 检查是否有足够的数据形成形态
        if (index < minTroughDistance * 3) {
            return false;
        }

        // 查找当前索引前的所有谷值
        List<Integer> troughs = findTroughs(lowPriceIndicator, index);

        // 需要至少3个谷值才能形成头肩底
        if (troughs.size() < 3) {
            return false;
        }

        // 检查所有可能的3个连续谷值组合
        for (int i = 0; i < troughs.size() - 2; i++) {
            int leftShoulderIdx = troughs.get(i);    // 左肩位置
            int headIdx = troughs.get(i + 1);        // 头部位置
            int rightShoulderIdx = troughs.get(i + 2); // 右肩位置

            if (isValidHeadAndShoulders(leftShoulderIdx, headIdx, rightShoulderIdx, index)) {
                System.out.println("左肩位置:"+leftShoulderIdx+",头部位置:"+headIdx+",右肩位置:"+rightShoulderIdx);
                return true;
            }
        }

        return false;
    }

    /**
     * 查找价格序列中的谷值
     *
     * @param indicator 价格指标
     * @param endIndex 结束索引
     * @return 谷值位置列表
     */
    private List<Integer> findTroughs(Indicator<Num> indicator, int endIndex) {
        List<Integer> troughs = new ArrayList<>();

        // 滑动窗口查找谷值
        for (int i = minTroughDistance; i <= endIndex - minTroughDistance; i++) {
            boolean isTrough = true;
            Num currentValue = indicator.getValue(i);

            // 检查当前值是否低于窗口内的邻居
            for (int j = i - minTroughDistance; j <= i + minTroughDistance; j++) {
                if (j == i) continue;
                if (indicator.getValue(j).isLessThanOrEqual(currentValue)) {
                    isTrough = false;
                    break;
                }
            }

            if (isTrough) {
                troughs.add(i);
            }
        }

        return troughs;
    }

    /**
     * 验证是否为有效的头肩底形态
     *
     * @param leftShoulderIdx 左肩索引
     * @param headIdx 头部索引
     * @param rightShoulderIdx 右肩索引
     * @param currentIndex 当前索引
     * @return 是否有效
     */
    private boolean isValidHeadAndShoulders(int leftShoulderIdx, int headIdx,
                                            int rightShoulderIdx, int currentIndex) {
        // 获取各位置价格
        Num leftShoulderPrice = lowPriceIndicator.getValue(leftShoulderIdx);
        Num headPrice = lowPriceIndicator.getValue(headIdx);
        Num rightShoulderPrice = lowPriceIndicator.getValue(rightShoulderIdx);

        // 条件1: 头部必须低于左右肩
        if (!headPrice.isLessThan(leftShoulderPrice) ||
                !headPrice.isLessThan(rightShoulderPrice)) {
            return false;
        }

        // 条件2: 左右肩高度应大致相当(在允许偏差范围内)
        double heightRatio = rightShoulderPrice.dividedBy(leftShoulderPrice).doubleValue();
        if (Math.abs(1 - heightRatio) > minHeightRatio) {
            return false;
        }

        // 条件3: 找到颈线起点(左肩和头部之间的最高点)
        int necklineStart = findNecklineStart(leftShoulderIdx, headIdx);
        if (necklineStart == -1) {
            return false;
        }

        // 条件4: 检查价格是否已突破颈线
        return isNecklineBroken(necklineStart, rightShoulderIdx, currentIndex);
    }

    /**
     * 查找颈线起点(左肩和头部之间的最高点)
     *
     * @param leftShoulderIdx 左肩索引
     * @param headIdx 头部索引
     * @return 颈线起点索引
     */
    private int findNecklineStart(int leftShoulderIdx, int headIdx) {
        int necklineStart = -1;
        Num maxPrice = getBarSeries().numFactory().numOf(Double.MIN_VALUE);

        // 遍历查找最高点
        for (int i = leftShoulderIdx + 1; i < headIdx; i++) {
            Num currentHigh = highPriceIndicator.getValue(i);
            if (currentHigh.isGreaterThan(maxPrice)) {
                maxPrice = currentHigh;
                necklineStart = i;
            }
        }

        return necklineStart;
    }

    /**
     * 检查价格是否突破颈线
     *
     * @param necklineStart 颈线起点索引
     * @param rightShoulderIdx 右肩索引
     * @param currentIndex 当前索引
     * @return 是否突破
     */
    private boolean isNecklineBroken(int necklineStart, int rightShoulderIdx, int currentIndex) {
        Num necklinePrice = highPriceIndicator.getValue(necklineStart);

        // 检查右肩后是否有收盘价高于颈线
        for (int i = rightShoulderIdx + 1; i <= currentIndex; i++) {
            if (closePriceIndicator.getValue(i).isGreaterThan(necklinePrice)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " 最小谷值距离: " + minTroughDistance +
                " 最小高度比例: " + minHeightRatio;
    }

    @Override
    public int getCountOfUnstableBars() {
        return 0;
    }
}