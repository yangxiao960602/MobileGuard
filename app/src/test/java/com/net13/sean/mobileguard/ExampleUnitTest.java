package com.net13.sean.mobileguard;

import android.test.AndroidTestCase;

import com.net13.sean.mobileguard.dao.BlackDao;
import com.net13.sean.mobileguard.domain.BlackTable;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends AndroidTestCase {
	@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

	public void testAddBlackNumber() {
		BlackDao dao = new BlackDao(getContext());
		dao.add("1383838438", BlackTable.SMS);
	}
}