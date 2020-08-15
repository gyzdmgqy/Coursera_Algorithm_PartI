/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:7/31/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {
    private final int nTeams;
    private final HashMap<String, Integer> team2ID;
    private final HashMap<Integer, String> iD2Team;
    private final int[] wins, losses, remaining;
    private final int[][] games;
    private boolean[] eliminated;
    private final List<List<String>> certificates;

    public BaseballElimination(String filename) {
        team2ID = new HashMap<>();
        iD2Team = new HashMap<>();
        // create a baseball division from given filename in format specified below
        In input = new In(filename);
        nTeams = input.readInt();
        wins = new int[nTeams];
        losses = new int[nTeams];
        remaining = new int[nTeams];
        eliminated = new boolean[nTeams];
        certificates = new ArrayList<>();
        games = new int[nTeams][nTeams];
        for (int i = 0; i < nTeams; ++i) {
            String team = input.readString();
            eliminated[i] = false;
            team2ID.put(team, i);
            iD2Team.put(i, team);
            wins[i] = input.readInt();
            losses[i] = input.readInt();
            remaining[i] = input.readInt();
            certificates.add(null);
            for (int j = 0; j < nTeams; ++j) games[i][j] = input.readInt();
        }
        checkTeamElimination();

    }

    private FlowNetwork createNetwork(int teamID) {
        int alreadyWins = wins[teamID] + remaining[teamID];
        int numMatches = (nTeams - 1) * (nTeams - 2) / 2;
        int nVertex = nTeams + 1 + numMatches;
        FlowNetwork network = new FlowNetwork(nVertex);
        int vertexGame = 1;
        for (int i = 0; i < nTeams; ++i) {
            if (i == teamID) continue;
            int teamI = i > teamID ? i + numMatches : i + numMatches + 1;
            for (int j = i + 1; j < nTeams; ++j) {
                if (j == teamID) continue;
                network.addEdge(new FlowEdge(0, vertexGame, games[i][j]));
                int teamJ = j > teamID ? j + numMatches : j + numMatches + 1;
                network.addEdge(new FlowEdge(vertexGame, teamI, Integer.MAX_VALUE));
                network.addEdge(new FlowEdge(vertexGame, teamJ, Integer.MAX_VALUE));
                vertexGame++;
            }
            int capacity = alreadyWins - wins[i] > 0 ? alreadyWins - wins[i] : 0;
            network.addEdge(new FlowEdge(teamI, nVertex - 1, capacity));
        }
        return network;
    }

    private void checkTeamElimination() {
        int numMatches = (nTeams - 1) * (nTeams - 2) / 2;
        int nVertex = nTeams + 1 + numMatches;
        int maxWins = 0;
        int teamIdWithMaxWin = 0;
        for (int i = 0; i < nTeams; ++i) {
            if (wins[i] > maxWins) {
                maxWins = wins[i];
                teamIdWithMaxWin = i;
            }
        }
        for (int i = 0; i < nTeams; ++i) {
            if (wins[i] + remaining[i] < maxWins) {
                eliminated[i] = true;
                certificates.set(i, Arrays.asList(iD2Team.get(teamIdWithMaxWin)));
                continue;
            }
            FlowNetwork network = createNetwork(i);
            FordFulkerson ford = new FordFulkerson(network, 0, nVertex - 1);
            List<String> certificate = new ArrayList<>();
            for (int j = 0; j < nTeams - 1; ++j) {
                int teamId = j < i ? j : j + 1;
                int vertexId = j + numMatches + 1;
                if (ford.inCut(vertexId)) certificate.add(iD2Team.get(teamId));
            }
            if (!certificate.isEmpty()) {
                certificates.set(i, certificate);
                eliminated[i] = true;
            }
        }
    }

    public int numberOfTeams()  // number of teams
    {
        return nTeams;

    }

    public Iterable<String> teams()                                // all teams
    {
        return team2ID.keySet();
    }

    public int wins(String team)                      // number of wins for given team
    {
        checkTeamValidity(team);
        return wins[team2ID.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        checkTeamValidity(team);
        return losses[team2ID.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        checkTeamValidity(team);
        return remaining[team2ID.get(team)];
    }

    public int against(String team1,
                       String team2)    // number of remaining games between team1 and team2
    {
        checkTeamValidity(team1);
        checkTeamValidity(team2);
        return games[team2ID.get(team1)][team2ID.get(team2)];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        checkTeamValidity(team);
        return eliminated[team2ID.get(team)];
    }

    public Iterable<String> certificateOfElimination(
            String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        checkTeamValidity(team);
        if (!eliminated[team2ID.get(team)]) return null;
        return certificates.get(team2ID.get(team));
    }

    private void checkTeamValidity(String team) {
        if (!team2ID.containsKey(team))
            throw new IllegalArgumentException("The input team is invalid!");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
