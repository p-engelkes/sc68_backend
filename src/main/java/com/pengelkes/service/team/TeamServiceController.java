package com.pengelkes.service.team;

import com.pengelkes.backend.jooq.tables.records.TeamRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pengelkes.backend.jooq.tables.Team.TEAM;

/**
 * Created by pengelkes on 02.12.2016.
 */
@Service
@Transactional
public class TeamServiceController
{
    private DSLContext dsl;

    @Autowired
    public TeamServiceController(DSLContext dsl)
    {
        this.dsl = dsl;
    }

    public Optional<Team> create(Team team)
    {
        TeamRecord record = dsl.insertInto(TEAM)
                .set(TEAM.NAME, team.getName())
                .set(TEAM.TRAINING_TIMES, team.getTrainingTimes())
                .returning(TEAM.ID)
                .fetchOne();

        team.setId(record.getId());
        return Optional.of(team);
    }

    public Optional<Team> findByName(String name)
    {
        return getEntity(dsl.selectFrom(TEAM).where(TEAM.NAME.eq(name)).fetchOne());
    }

    public Optional<Team> findById(int id)
    {
        return getEntity(dsl.selectFrom(TEAM).where(TEAM.ID.eq(id)).fetchOne());
    }

    public List<Team> getAllTeams()
    {
        return getEntities(dsl.selectFrom(TEAM).fetch());
    }

    private Optional<Team> getEntity(Record record)
    {
        if (record != null)
        {
            int id = record.getValue(TEAM.ID, Integer.class);
            String name = record.getValue(TEAM.NAME, String.class);

            return Optional.of(new Team(id, name));
        }

        return Optional.empty();
    }

    private List<Team> getEntities(Result<TeamRecord> result)
    {
        List<Team> allTeams = new ArrayList<>();
        result.forEach(record -> getEntity(record).ifPresent(allTeams::add));

        return allTeams;
    }
}
