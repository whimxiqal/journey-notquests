/*
 * MIT License
 *
 * Copyright (c) whimxiqal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.whimxiqal.journey.integration.notquests;

import java.util.List;
import net.whimxiqal.journey.Cell;
import net.whimxiqal.journey.JourneyApiProvider;
import net.whimxiqal.journey.bukkit.JourneyBukkitApiProvider;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rocks.gravili.notquests.paper.events.notquests.QuestFinishAcceptEvent;
import rocks.gravili.notquests.paper.structs.ActiveObjective;
import rocks.gravili.notquests.paper.structs.objectives.Objective;

public class StartQuestListener implements Listener {

  @EventHandler
  public void onStartQuest(QuestFinishAcceptEvent event) {
    List<ActiveObjective> activeObjectives = event.getActiveQuest().getActiveObjectives();
    if (activeObjectives.isEmpty()) {
      return;
    }
    Objective firstObjective = activeObjectives.get(0).getObjective();
    Location location = firstObjective.getLocation();
    if (location == null) {
      return;  // there was no location associated with this objective
    }

    if (!firstObjective.getConfig().getBoolean(firstObjective.getInitialConfigPath() + ".journey", false)) {
      return;  // config doesn't specify to use journey
    }

    Cell cell = JourneyBukkitApiProvider.get().toCell(firstObjective.getLocation());
    JourneyApiProvider.get().runPlayerDestinationSearch(event.getQuestPlayer().getUniqueId(), cell, true);
  }

}
