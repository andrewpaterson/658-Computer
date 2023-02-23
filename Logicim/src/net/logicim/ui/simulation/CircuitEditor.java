package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.geometry.LineMinimiser;
import net.logicim.common.geometry.LinePositionCache;
import net.logicim.common.geometry.LineSplitter;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Positions;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.clipboard.ClipboardData;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceFinder;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.connection.LocalConnectionNet;
import net.logicim.ui.connection.PortTraceFinder;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.selection.Selection;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected Circuit circuit;
  protected Simulation simulation;
  protected Selection selection;

  protected ConnectionViewCache connectionViewCache;

  public CircuitEditor()
  {
    this.circuit = new Circuit();
    this.simulation = circuit.resetSimulation();
    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.selection = new Selection();
    this.connectionViewCache = new ConnectionViewCache();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    long time = getTime();
    List<View> views;
    synchronized (this)
    {
      views = getAllViews();
    }

    for (View view : views)
    {
      view.paint(graphics, viewport, time);
    }
  }

  protected List<View> getAllViews()
  {
    List<View> views = new ArrayList<>(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  protected long getTime()
  {
    return simulation.getTime();
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void deleteComponentView(StaticView<?> staticView)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteComponentViews(staticViews);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("Cannot delete a [null] view.");
      }

      connectionViews.addAll(disconnectStaticView(componentView));
      if (componentView instanceof IntegratedCircuitView)
      {
        deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) componentView);
      }
      else if (componentView instanceof PassiveView)
      {
        deletePassiveView((PassiveView<?, ?>) componentView);
      }
      else if (componentView instanceof DecorativeView)
      {
        removeDecorativeView((DecorativeView<?>) componentView);
      }
      else if (componentView instanceof TunnelView)
      {
        removeTunnelView((TunnelView) componentView);
      }
      else
      {
        throw new SimulatorException("Cannot delete view of class [%s].", componentView.getClass().getSimpleName());
      }
    }

    Set<ConnectionView> updatedConnectionViews = connectConnectionViews(connectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  protected void deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    circuit.remove(integratedCircuit);
    removeIntegratedCircuitView(integratedCircuitView);
  }

  protected void deletePassiveView(PassiveView<?, ?> passiveView)
  {
    Passive powerSource = passiveView.getComponent();
    circuit.remove(powerSource);
    removePassiveView(passiveView);
  }

  protected void disconnectTraceView(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnections();
    connectionViewCache.removeAll(traceView, connectionViews);
    traceView.disconnect();
  }

  public List<ConnectionView> disconnectStaticView(StaticView<?> staticView)
  {
    if (staticView == null)
    {
      throw new SimulatorException("Cannot disconnect [null] view.");
    }

    List<ConnectionView> connectionViews = staticView.getConnections();
    if (connectionViews == null)
    {
      throw new SimulatorException("Cannot disconnect %s with [null] connections.", staticView.toIdentifierString());
    }

    connectionViewCache.removeAll(staticView, connectionViews);
    staticView.disconnect(simulation);

    Component component = staticView.getComponent();
    if (component != null)
    {
      circuit.disconnectComponent(component, simulation);
    }

    return connectionViews;
  }

  private Set<ConnectionView> connectConnectionViews(Set<ConnectionView> connectionViews)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, connectionView);
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
    }
    return updatedConnectionViews;
  }

  public void editActionDeleteTraceView(ConnectionView connectionView, TraceView traceView)
  {
    if (connectionView instanceof HoverConnectionView)
    {
      Set<TraceView> traceViews = new LinkedHashSet<>();
      traceViews.add(traceView);
      deleteTraceViews(traceViews);
    }
    else
    {
      editActionDeleteTraceViews(connectionView);
    }
  }

  public boolean editActionDeleteTraceViews(ConnectionView connectionView)
  {
    List<View> connectedComponents = connectionView.getConnectedComponents();
    Set<TraceView> traceViews = new LinkedHashSet<>();
    for (View connectedComponent : connectedComponents)
    {
      if (connectedComponent instanceof TraceView)
      {
        traceViews.add((TraceView) connectedComponent);
      }
    }

    if (traceViews.size() > 0)
    {
      deleteTraceViews(traceViews);
      return true;
    }
    else
    {
      return false;
    }
  }

  public Simulation reset()
  {
    simulation = circuit.resetSimulation();
    return simulation;
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public StaticView<?> getComponentViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = getComponentViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      StaticView<?> closestView = null;
      for (StaticView<?> view : selectedViews)
      {
        if (view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension))
        {
          boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
          float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
          if (distance < shortestDistance)
          {
            closestView = view;
            shortestDistance = distance;
          }
        }
      }
      return closestView;
    }
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews, integratedCircuitViews, passiveViews, decorativeViews);
  }

  public List<StaticView<?>> getComponentViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<StaticView<?>> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> view = iterator.next();
      if (isInScreenSpaceBoundingBox(viewport, screenPosition, view))
      {
        selectedViews.add(view);
      }
    }
    return selectedViews;
  }

  protected boolean isInScreenSpaceBoundingBox(Viewport viewport, Int2D screenPosition, StaticView<?> view)
  {
    if (view.isEnabled())
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
      if (BoundingBox.containsPoint(screenPosition, boundBoxPosition, boundBoxDimension))
      {
        return true;
      }
    }
    return false;
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    return getTraceViewsInGridSpace(x, y);
  }

  protected List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : this.traceViews)
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceOverlap> getTracesTouching(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      LineOverlap overlap = traceView.touches(line);
      if (overlap != LineOverlap.None)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public ConnectionView getOrAddConnection(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnection(position, view);
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    for (TraceView traceView : traceViews)
    {
      disconnectTraceView(traceView);
      removeTraceView(traceView);
    }
  }

  public void validateConsistency()
  {
    validateConnectionViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();
  }

  private void validateConnectionViews()
  {
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      if (staticView instanceof ComponentView)
      {
        ComponentView<?> componentView = (ComponentView<?>) staticView;
        List<PortView> portViews = componentView.getPorts();
        for (PortView portView : portViews)
        {
          Int2D portPosition = portView.getGridPosition();
          ConnectionView connectionView = portView.getConnection();
          if (connectionView != null)
          {
            Int2D connectionPosition = connectionView.getGridPosition();
            if (connectionPosition == null)
            {
              throw new SimulatorException("Position on connection on %s cannot be null.", componentView.toIdentifierString());
            }
            if (!portPosition.equals(connectionPosition))
            {
              throw new SimulatorException("%s port [%s] position (%s) must be equal to connection position (%s).",
                                           componentView.toIdentifierString(),
                                           portView.getText(),
                                           Int2D.toString(portPosition),
                                           Int2D.toString(connectionPosition));
            }
          }
        }
      }

      List<ConnectionView> localConnectionViews = staticView.getConnections();
      for (ConnectionView connectionView : localConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection on %s cannot be null.", staticView.toIdentifierString());
        }
        Int2D connectionPosition = connectionView.getGridPosition();
        if (connectionPosition == null)
        {
          throw new SimulatorException("Position on connection on %s cannot be null.", staticView.toIdentifierString());
        }

        ConnectionView cacheConnectionView = connectionViewCache.getConnection(connectionPosition);
        if (cacheConnectionView != connectionView)
        {
          throw new SimulatorException("%s connection (%s) must be equal to cache connection (%s).",
                                       staticView.toIdentifierString(),
                                       Int2D.toString(connectionPosition),
                                       ConnectionView.toPositionString(cacheConnectionView));
        }
      }
    }

    connectionViewCache.validatePositions();
  }

  protected void validateTunnelViews()
  {
    for (TunnelView tunnelView : tunnelViews)
    {
      if (!tunnelView.isRemoved())
      {
        String name = tunnelView.getName();
        String sanitisedName = name.trim().toLowerCase();
        if (!tunnelView.getSanitisedName().equals(sanitisedName))
        {
          throw new SimulatorException("Tunnel view sanitised name is [%s] but should be [%s].", tunnelView.getSanitisedName(), sanitisedName);
        }

        Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
        if (!sanitisedName.isEmpty())
        {
          if (tunnelViews == null)
          {
            throw new SimulatorException("TunnelViewsMap did not contain a map for name [%s]", tunnelView.getName());
          }
          if (!tunnelViews.contains(tunnelView))
          {
            throw new SimulatorException("TunnelViewsMap did not contain tunnel view [%s].", tunnelView.getName());
          }
        }
      }
    }

    for (String sanitisedName : tunnelViewsMap.keySet())
    {
      Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
      for (TunnelView tunnelView : tunnelViews)
      {
        if (!this.tunnelViews.contains(tunnelView))
        {
          throw new SimulatorException("TunnelViews did not contain tunnel view [%s].", tunnelView.getName());
        }
      }
    }
  }

  protected void validateTracesContainOnlyCurrentViews()
  {
    for (TraceView traceView : traceViews)
    {
      if (traceView.hasConnections())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        for (View view : startConnection.getConnectedComponents())
        {
          boolean contained = true;
          if (view instanceof IntegratedCircuitView)
          {
            if (!integratedCircuitViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TraceView)
          {
            if (!traceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof PassiveView)
          {
            if (!passiveViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof DecorativeView)
          {
            if (!decorativeViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TunnelView)
          {
            if (!tunnelViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view == null)
          {
            throw new SimulatorException("TraceView [" + traceView.getDescription() + "] does not include trace has null connection.");
          }
          else
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] has not been included in validateConsistency.");
          }

          if (!contained)
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] does not include trace.");
          }
        }
      }
    }
  }

  public CircuitData save()
  {
    Set<View> selection = new HashSet<>(this.selection.getSelection());
    ArrayList<StaticData<?>> componentDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }

      componentDatas.add(staticData);
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    TimelineData timelineData = simulation.getTimeline().save();
    return new CircuitData(timelineData,
                           componentDatas,
                           traceDatas);
  }

  public ClipboardData copyViews(List<View> views)
  {
    ArrayList<StaticData<?>> componentDatas = new ArrayList<>();
    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (View view : views)
    {
      if (view instanceof StaticView)
      {
        StaticView<?> staticView = (StaticView<?>) view;
        StaticData<?> staticData = (StaticData<?>) staticView.save(false);
        if (staticData == null)
        {
          throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
        }

        componentDatas.add(staticData);
      }
      else if (view instanceof TraceView)
      {
        TraceView traceView = (TraceView) view;
        TraceData traceData = traceView.save(false);
        traceDatas.add(traceData);
      }
    }

    return new ClipboardData(componentDatas, traceDatas);
  }

  public void load(CircuitData circuitData)
  {
    selection.clearSelection();
    simulation.getTimeline().load(circuitData.timeline);

    loadViews(circuitData.traces, circuitData.components, true);
  }

  public List<View> loadViews(List<TraceData> traces, List<StaticData<?>> components, boolean createConnections)
  {
    ArrayList<View> views = new ArrayList<>();
    TraceLoader traceLoader = null;
    if (createConnections)
    {
      traceLoader = new TraceLoader();
    }

    for (TraceData traceData : traces)
    {
      TraceView traceView = traceData.create(this, traceLoader, createConnections);
      views.add(traceView);
    }

    for (StaticData<?> staticData : components)
    {
      StaticView<?> staticView = staticData.createAndLoad(this, traceLoader, createConnections);
      views.add(staticView);
    }
    return views;
  }

  public Set<ConnectionView> connectStaticView(StaticView<?> staticView)
  {
    ArrayList<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);

    List<ConnectionView> connectionViews = createStaticViewConnections(staticViews);
    Set<ConnectionView> updatedConnectionViews = createConnectConnectionViewTraces(connectionViews);
    enableStaticViews(staticViews);

    return updatedConnectionViews;
  }

  protected List<ConnectionView> createStaticViewConnections(List<StaticView<?>> staticViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.createConnections(this));
    }
    return connectionViews;
  }

  private void enableStaticViews(List<StaticView<?>> staticViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.enable(simulation);
      staticView.simulationStarted(simulation);
    }
  }

  protected Set<ConnectionView> createConnectConnectionViewTraces(List<ConnectionView> connectionViews)
  {
    Set<ConnectionView> updatedConnectionViews = new HashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(simulation, connectionView);
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
    }

    return updatedConnectionViews;
  }

  public Set<PortView> getPortViews(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = new HashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View view : connectedComponents)
      {
        if (view instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) view;
          PortView portView = componentView.getPort(connectionView);
          portViews.add(portView);
        }
      }
    }
    return portViews;
  }

  public void fireConnectionEvents(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = getPortViews(connectionViews);
    for (PortView portView : portViews)
    {
      portView.traceConnected(simulation);
    }
  }

  public Timeline getTimeline()
  {
    return simulation.getTimeline();
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  public void placeComponentView(StaticView<?> staticView)
  {
    Set<ConnectionView> updatedConnectionViews = connectStaticView(staticView);

    fireConnectionEvents(updatedConnectionViews);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews)
  {
    List<TraceView> connectedTraceViews = findImmediateConnectedTraceViews(staticViews);

    Set<TraceView> allConnectedTraceViews = new HashSet<>(connectedTraceViews);
    allConnectedTraceViews.removeAll(traceViews);

    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      connectionViews.addAll(disconnectStaticView(staticView));
    }

    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnections());
      disconnectTraceView(traceView);
    }

    connectConnectionViews(connectionViews);
    selection.clearSelection();

    recreateTraceViews(new HashSet<>(), allConnectedTraceViews);
  }

  protected List<TraceView> findImmediateConnectedTraceViews(List<StaticView<?>> staticViews)
  {
    List<TraceView> connectedTraceViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      List<ConnectionView> connectionViews = staticView.getConnections();
      for (ConnectionView connectionView : connectionViews)
      {
        List<View> connectedComponents = connectionView.getConnectedComponents();
        for (View connectedComponent : connectedComponents)
        {
          if (connectedComponent instanceof TraceView)
          {
            connectedTraceViews.add((TraceView) connectedComponent);
          }
        }
      }
    }
    return connectedTraceViews;
  }

  public Set<TraceView> createTraceViews(List<Line> inputLines)
  {
    Set<Line> lines = new LinkedHashSet<>(inputLines);
    TraceFinder traceFinder = new TraceFinder();
    for (Line line : inputLines)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(line);
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        traceFinder.add(traceOverlap.getTraceView());
      }
    }

    traceFinder.process();
    Set<TraceView> touchingTraceViews = traceFinder.getTraceViews();
    return recreateTraceViews(lines, touchingTraceViews);
  }

  protected Set<TraceView> recreateTraceViews(Set<Line> lines, Set<TraceView> touchingTraceViews)
  {
    for (TraceView traceView : touchingTraceViews)
    {
      lines.add(traceView.getLine());
    }
    removeTraceViews(touchingTraceViews);
    Set<TraceView> traceViews = generateNewTraces(lines);

    connectCreatedTraces(traceViews);
    return traceViews;
  }

  public void doneMoveComponents(List<StaticView<?>> staticViews, List<TraceView> traceViews, Set<StaticView<?>> selectedViews)
  {
    List<Line> newLines = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      newLines.add(traceView.getLine());
    }
    removeTraceViews(new LinkedHashSet<>(traceViews));

    List<ConnectionView> connectionViews = createStaticViewConnections(staticViews);
    Set<ConnectionView> junctions = getComponentConnectionPositions(staticViews);
    Set<TraceView> existingTraceViews = new LinkedHashSet<>();
    for (ConnectionView junction : junctions)
    {
      Int2D position = junction.getGridPosition();
      existingTraceViews.addAll(getTraceViewsInGridSpace(position.x, position.y));
    }

    List<Line> existingLines = new ArrayList<>();
    for (TraceView existingTraceView : existingTraceViews)
    {
      existingLines.add(existingTraceView.getLine());
    }
    removeTraceViews(existingTraceViews);

    Set<ConnectionView> updatedConnectionViews = createConnectConnectionViewTraces(connectionViews);
    enableStaticViews(staticViews);

    List<View> newSelection = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      if (selectedViews.contains(staticView))
      {
        newSelection.add(staticView);
      }
    }

    Set<TraceView> existingTraces = createTraceViews(existingLines);
    connectCreatedTraces(existingTraces);

    Set<TraceView> newTraces = createTraceViews(newLines);
    connectCreatedTraces(newTraces);
    newSelection.addAll(newTraces);

    fireConnectionEvents(updatedConnectionViews);

    this.selection.setSelection(newSelection);
  }

  protected Set<ConnectionView> getComponentConnectionPositions(List<StaticView<?>> staticViews)
  {
    Set<ConnectionView> junctions = new HashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      List<ConnectionView> connectionViews = staticView.getConnections();
      for (ConnectionView connectionView : connectionViews)
      {
        junctions.add(connectionView);
      }
    }
    return junctions;
  }

  public Selection getSelection()
  {
    return selection;
  }

  public List<View> getSelectionFromRectangle(Float2D start, Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      updateSelectedViews(start, end, includeIntersections, boundBoxPosition, boundBoxDimension, selectedViews, staticView);
    }

    for (TraceView traceView : traceViews)
    {
      Line line = traceView.getLine();
      line.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (line.isPositionOn(new Int2D(start)))
        {
          selectedViews.add(traceView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(traceView);
        }
      }
    }
    return selectedViews;
  }

  protected void updateSelectedViews(Float2D start,
                                     Float2D end,
                                     boolean includeIntersections,
                                     Float2D boundBoxPosition,
                                     Float2D boundBoxDimension,
                                     List<View> selectedViews,
                                     StaticView<?> componentView)
  {
    if (componentView.isEnabled())
    {
      componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (BoundingBox.containsPoint(new Int2D(start), boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(componentView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(componentView);
        }
      }
    }
  }

  protected boolean isPoint(Float2D start, Float2D end)
  {
    return (Math.round(start.x) == Math.round(end.x)) &&
           (Math.round(start.y) == Math.round(end.y));
  }

  public void connectCreatedTraces(Set<TraceView> traceViews)
  {
    Set<ConnectionView> connectionViews = new HashSet<>();
    Set<ConnectionView> updatedConnectionViews;
    int i = 0;
    for (TraceView traceView : traceViews)
    {
      if (!traceView.hasConnections())
      {
        throw new SimulatorException("Cannot finalise a removed Trace.  Iteration [%s].", i);
      }
      connectionViews.add(traceView.getStartConnection());
      i++;
    }
    updatedConnectionViews = connectConnectionViews(connectionViews);

    fireConnectionEvents(updatedConnectionViews);
  }

  public void deleteSelection()
  {
    Set<TraceView> traceViews = new HashSet<>();
    List<View> selectedViews = selection.getSelection();
    for (View view : selectedViews)
    {
      if (view instanceof TraceView)
      {
        traceViews.add((TraceView) view);
      }
    }
    deleteTraceViews(traceViews);

    List<StaticView<?>> staticViews = new ArrayList<>();
    for (View view : selectedViews)
    {
      if (view instanceof StaticView)
      {
        staticViews.add((StaticView<?>) view);
      }
    }
    deleteComponentViews(staticViews);
    selection.clearSelection();
  }

  private void deleteTraceViews(Set<TraceView> inputTraceViews)
  {
    TraceFinder traceFinder = new TraceFinder();
    for (TraceView traceView : inputTraceViews)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(traceView.getLine());
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        TraceView touchingTraceView = traceOverlap.getTraceView();
        if (!inputTraceViews.contains(touchingTraceView))
        {
          traceFinder.add(touchingTraceView);
        }
      }
    }

    removeTraceViews(inputTraceViews);

    Set<Line> lines = new LinkedHashSet<>();
    traceFinder.process();
    Set<TraceView> touchingTraceViews = traceFinder.getTraceViews();
    recreateTraceViews(lines, touchingTraceViews);
  }

  private Set<TraceView> generateNewTraces(Set<Line> lines)
  {
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Positions positionMap = new Positions(lines);

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    StaticViewIterator iterator = staticViewIterator();

    List<Int2D> additionalJunctions = new ArrayList<>();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      List<ConnectionView> connections = staticView.getConnections();
      for (ConnectionView connection : connections)
      {
        Int2D position = connection.getGridPosition();
        if (lineCache.touchesLine(position.x, position.y))
        {
          additionalJunctions.add(position.clone());
        }
      }
    }

    Set<Line> splitLines = LineSplitter.split(lineCache, positionMap, additionalJunctions);

    Set<TraceView> traceViews = new LinkedHashSet<>();
    for (Line line : splitLines)
    {
      traceViews.add(new TraceView(this, line));
    }
    return traceViews;
  }

  public StaticView<?> getSingleSelectionStaticView()
  {
    StaticView<?> componentView = null;
    for (View view : selection.getSelection())
    {
      if (view instanceof StaticView)
      {
        if (componentView == null)
        {
          componentView = (StaticView<?>) view;
        }
        else
        {
          return null;
        }
      }
    }
    return componentView;
  }

  public void select(View view)
  {
    selection.add(view);
  }

  public void replaceSelection(View newView, View oldView)
  {
    selection.replaceSelection(newView, oldView);
  }

  public void addIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.add(integratedCircuitView);
    }
  }

  public void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  public void addDecorativeView(DecorativeView<?> view)
  {
    synchronized (this)
    {
      decorativeViews.add(view);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  protected boolean removeDecorativeView(DecorativeView<?> decorativeView)
  {
    synchronized (this)
    {
      return decorativeViews.remove(decorativeView);
    }
  }

  public void addTraceView(TraceView view)
  {
    synchronized (this)
    {
      traceViews.add(view);
    }
  }

  public void removeTraceView(TraceView traceView)
  {
    if (!traceView.hasConnections())
    {
      synchronized (this)
      {
        if (!traceViews.remove(traceView))
        {
          throw new SimulatorException("Cannot remove trace not in circuit editor.");
        }
      }
    }
    else
    {
      throw new SimulatorException("Cannot remove trace view with connections.");
    }
  }

  protected boolean removeTunnelView(TunnelView tunnelView)
  {
    synchronized (this)
    {
      String name = tunnelView.getSanitisedName();
      boolean removed = this.tunnelViews.remove(tunnelView);
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        tunnelViews.remove(tunnelView);
        if (tunnelViews.isEmpty())
        {
          this.tunnelViewsMap.remove(name);
        }
      }
      return removed;
    }
  }

  public Set<TunnelView> addTunnel(TunnelView tunnelView)
  {
    synchronized (this)
    {
      this.tunnelViews.add(tunnelView);

      String name = tunnelView.getSanitisedName();
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        if (tunnelViews == null)
        {
          tunnelViews = new LinkedHashSet<>();
          this.tunnelViewsMap.put(name, tunnelViews);
        }
        tunnelViews.add(tunnelView);
        return tunnelViews;
      }

      return null;
    }
  }

  public ConnectionView getConnection(int x, int y)
  {
    return connectionViewCache.getConnection(x, y);
  }

  public boolean isSelectionEmpty()
  {
    return getSelection().isSelectionEmpty();
  }
}

