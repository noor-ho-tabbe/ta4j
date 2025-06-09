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

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

import static org.junit.Assert.assertTrue;

public class HeadAndShouldersBottomIndicatorTest extends AbstractIndicatorTest<Indicator<Boolean>, Num> {

    private BarSeries series;

    public HeadAndShouldersBottomIndicatorTest(NumFactory numFactory) {
        super(numFactory);
    }

    @Before
    public void setUp() {
        series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();
        series.barBuilder().openPrice(70.0).closePrice(101.0).highPrice(80.0).lowPrice(98.0).add();
        series.barBuilder().openPrice(72.0).closePrice(101.0).highPrice(82.0).lowPrice(98.0).add();
        series.barBuilder().openPrice(74.0).closePrice(101.0).highPrice(83.0).lowPrice(98.0).add();
        series.barBuilder().openPrice(100.0).closePrice(101.0).highPrice(102.0).lowPrice(98.0).add();
        series.barBuilder().openPrice(101.0).closePrice(107.0).highPrice(90.0).lowPrice(100.0).add();
        series.barBuilder().openPrice(107.0).closePrice(103.0).highPrice(89.0).lowPrice(102.0).add();
        series.barBuilder().openPrice(103.0).closePrice(102.0).highPrice(88.0).lowPrice(100.0).add();
        series.barBuilder().openPrice(102.0).closePrice(102.0).highPrice(110.0).lowPrice(101.0).add();
        series.barBuilder().openPrice(102.0).closePrice(114.0).highPrice(88.0).lowPrice(102.0).add();
        series.barBuilder().openPrice(114.0).closePrice(113.0).highPrice(89.0).lowPrice(112.0).add();
        series.barBuilder().openPrice(113.0).closePrice(107.0).highPrice(88.0).lowPrice(106.0).add();
        series.barBuilder().openPrice(107.0).closePrice(106.0).highPrice(90.0).lowPrice(105.0).add();
        series.barBuilder().openPrice(106.0).closePrice(109.0).highPrice(89.0).lowPrice(105.0).add();
        series.barBuilder().openPrice(109.0).closePrice(103.0).highPrice(88.0).lowPrice(102.0).add();
        series.barBuilder().openPrice(103.0).closePrice(99.0).highPrice(87.0).lowPrice(98.0).add();
        series.barBuilder().openPrice(99.0).closePrice(96.0).highPrice(88.0).lowPrice(95.0).add();
    }

    @Test
    public void getValue() {
        var tws = new HeadAndShouldersTopIndicator(series);
//        assertFalse(tws.getValue(0));
//        assertFalse(tws.getValue(1));
//        assertFalse(tws.getValue(2));
//        assertFalse(tws.getValue(3));
//        assertFalse(tws.getValue(4));
//        assertFalse(tws.getValue(5));
//        assertFalse(tws.getValue(6));
//        assertFalse(tws.getValue(7));
//        assertFalse(tws.getValue(8));
//        assertFalse(tws.getValue(9));
//        assertFalse(tws.getValue(10));
//        assertTrue(tws.getValue(11));
//        assertTrue(tws.getValue(12));
        assertTrue(tws.getValue(19));
    }
}
