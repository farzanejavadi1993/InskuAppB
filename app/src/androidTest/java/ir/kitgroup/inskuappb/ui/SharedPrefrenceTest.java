package ir.kitgroup.inskuappb.ui;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import com.orm.SugarContext;
import com.orm.query.Select;

import java.util.List;

import ir.kitgroup.inskuappb.dataBase.Guild;

@RunWith(AndroidJUnit4.class)
public class SharedPrefrenceTest {


    @Before
    public void setUp() {
        SugarContext.init(ApplicationProvider.getApplicationContext());

    }


    @After
    public void tearDown() {
        SugarContext.terminate();
    }

    @Test
    public void insertGuildTest() {

        Guild guild = new Guild();
        guild.setI("1");
        guild.setName("John Doe");
        guild.save();

        Guild insertedGuild = Guild.find(Guild.class, "name = ?", "John Doe").get(0);
        assertNotNull(insertedGuild);
        assertEquals("John Doe", insertedGuild.getName());
        assertEquals("1", insertedGuild.getI());
    }

    @Test
    public void deleteGuildTest() {

        Guild guild = new Guild();
        guild.setI("1");
        guild.setName("John Doe");
        guild.save();

        List<Guild> allGuild = Select.from(Guild.class).list();

        assertNotNull(allGuild);

        assertThat(allGuild).doesNotContain(guild);

    }


}