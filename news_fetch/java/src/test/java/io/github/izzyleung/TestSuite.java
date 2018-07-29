package io.github.izzyleung;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    Stories_FromJsonTests.class,
    ZhihuDailyOfficial_NewsFromTest.class
})
public class TestSuite {

}
