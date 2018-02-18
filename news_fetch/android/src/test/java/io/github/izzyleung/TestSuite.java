package io.github.izzyleung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ZhihuDailyOfficial_StoriesTests.class,
    ZhihuDailyOfficial_ConvertToNewsTest.class
})
public class TestSuite {

}
