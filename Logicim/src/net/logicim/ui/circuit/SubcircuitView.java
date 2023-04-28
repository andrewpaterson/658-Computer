package net.logicim.ui.circuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.geometry.LineMinimiser;
import net.logicim.common.geometry.LinePositionCache;
import net.logicim.common.geometry.LineSplitter;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Positions;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.passive.common.Passive;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.LineOverlap;
import net.logicim.ui.common.TraceOverlap;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceFinder;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.connection.LocalConnectionNet;
import net.logicim.ui.connection.PortTraceFinder;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.ConnectionViewCache;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.TopLevelSubcircuitSimulation;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.*;

public class SubcircuitView
{
  protected String typeName;  //The subcircuit instance name is on the StaticSubcircuitView.

  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected ConnectionViewCache connectionViewCache;

  public SubcircuitView()
  {
    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.connectionViewCache = new ConnectionViewCache();
  }

  public List<View> getAllViews()
  {
    List<View> views = new ArrayList<>(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  public void disconnectTraceView(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnections();
    connectionViewCache.removeAll(traceView, connectionViews);
    traceView.disconnect();
  }

  public List<ConnectionView> disconnectStaticView(StaticView<?> staticView, CircuitSimulation circuitSimulation)
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
    staticView.disconnect();

    if (staticView instanceof ComponentView)
    {
      Component component = ((ComponentView<?>) staticView).getComponent(circuitSimulation);
      if (component != null)
      {
        Simulation simulation = circuitSimulation.getSimulation();
        Circuit circuit = circuitSimulation.getCircuit();
        circuit.disconnectComponent(component, simulation);
      }
    }

    return connectionViews;
  }

  public void deleteComponentView(StaticView<?> staticView,
                                  CircuitSimulation circuitSimulation)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteComponentViews(staticViews, circuitSimulation);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews,
                                   CircuitSimulation circuitSimulation)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("Cannot delete a [null] view.");
      }

      connectionViews.addAll(disconnectStaticView(componentView, circuitSimulation));
      if (componentView instanceof IntegratedCircuitView)
      {
        deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) componentView, circuitSimulation);
      }
      else if (componentView instanceof PassiveView)
      {
        deletePassiveView((PassiveView<?, ?>) componentView, circuitSimulation);
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

    Set<ConnectionView> updatedConnectionViews = connectConnectionViews(connectionViews, circuitSimulation);
    fireConnectionEvents(updatedConnectionViews, circuitSimulation);
  }

  public void fireConnectionEvents(Set<ConnectionView> connectionViews,
                                   CircuitSimulation circuitSimulation)
  {
    Set<PortView> portViews = getPortViews(connectionViews);
    for (PortView portView : portViews)
    {
      portView.traceConnected(circuitSimulation);
    }
  }

  protected void deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView,
                                         CircuitSimulation simulation)
  {
    if (simulation != null)
    {
      IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getComponent(simulation);
      simulation.getCircuit().remove(integratedCircuit);
    }
    removeIntegratedCircuitView(integratedCircuitView);
  }

  protected void deletePassiveView(PassiveView<?, ?> passiveView,
                                   CircuitSimulation simulation)
  {
    if (simulation != null)
    {
      Passive passive = passiveView.getComponent(simulation);
      simulation.getCircuit().remove(passive);
    }
    removePassiveView(passiveView);
  }

  public ConnectionView getOrAddConnectionView(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnectionView(position, view);
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews, integratedCircuitViews, passiveViews, decorativeViews);
  }

  public Set<IntegratedCircuitView<?, ?>> getIntegratedCircuitViews()
  {
    return integratedCircuitViews;
  }

  public Set<PassiveView<?, ?>> getPassiveViews()
  {
    return passiveViews;
  }

  public Set<TunnelView> getTunnelViews()
  {
    return tunnelViews;
  }

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
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
        List<PortView> portViews = componentView.getPortViews();
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

        ConnectionView cacheConnectionView = connectionViewCache.getConnectionView(connectionPosition);
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
    return connectionViewCache.getConnectionView(x, y);
  }

  public Set<ConnectionView> connectConnectionViews(Collection<ConnectionView> connectionViews,
                                                    CircuitSimulation circuitSimulation)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    if (circuitSimulation != null)
    {
      for (ConnectionView connectionView : connectionViews)
      {
        if (!updatedConnectionViews.contains(connectionView))
        {
          List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(circuitSimulation, connectionView);
          updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
        }
      }

    }
    return updatedConnectionViews;
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    for (TraceView traceView : traceViews)
    {
      disconnectTraceView(traceView);
      removeTraceView(traceView);
    }
  }

  public Set<ConnectionView> connectStaticView(StaticView<?> staticView,
                                               CircuitSimulation circuitSimulation)
  {
    ArrayList<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);

    List<ConnectionView> connectionViews = createStaticViewConnections(staticViews);
    Set<ConnectionView> updatedConnectionViews = connectConnectionViews(connectionViews, circuitSimulation);
    enableStaticViews(staticViews, circuitSimulation);

    return updatedConnectionViews;
  }

  public List<ConnectionView> createStaticViewConnections(List<StaticView<?>> staticViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.createConnectionViews(this));
    }
    return connectionViews;
  }

  public void enableStaticViews(List<StaticView<?>> staticViews, CircuitSimulation simulation)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.enable();
      staticView.simulationStarted(simulation);
    }

    if (simulation != null)
    {
      for (StaticView<?> staticView : staticViews)
      {
        staticView.simulationStarted(simulation);
      }
    }
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

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : getTraceViews())
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public List<TraceOverlap> getTracesTouching(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : getTraceViews())
    {
      LineOverlap overlap = traceView.touches(line);
      if (overlap != LineOverlap.None)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public Set<TraceView> createTraceViews(List<Line> inputLines, CircuitSimulation circuitSimulation)
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
    return recreateTraceViews(lines, touchingTraceViews, circuitSimulation);
  }

  protected Set<TraceView> recreateTraceViews(Set<Line> lines,
                                              Set<TraceView> touchingTraceViews,
                                              CircuitSimulation circuitSimulation)
  {
    for (TraceView traceView : touchingTraceViews)
    {
      lines.add(traceView.getLine());
    }
    removeTraceViews(touchingTraceViews);
    Set<TraceView> traceViews = generateNewTraces(lines);

    Set<ConnectionView> updatedConnectionViews = connectCreatedTraces(traceViews, circuitSimulation);
    fireConnectionEvents(updatedConnectionViews, circuitSimulation);
    return traceViews;
  }

  public void deleteTraceViews(Set<TraceView> inputTraceViews,
                               CircuitSimulation circuitSimulation)
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

    Set<ConnectionView> nonTraceConnectionViews = findNonTraceConnections(inputTraceViews);
    removeTraceViews(inputTraceViews);

    Set<Line> lines = new LinkedHashSet<>();
    traceFinder.process();
    Set<TraceView> touchingTraceViews = traceFinder.getTraceViews();
    recreateTraceViews(lines, touchingTraceViews, circuitSimulation);

    findAndConnectNonTraceViewsForDeletion(circuitSimulation, nonTraceConnectionViews);
  }

  protected void findAndConnectNonTraceViewsForDeletion(CircuitSimulation circuitSimulation,
                                                        Set<ConnectionView> nonTraceConnectionViews)
  {
    if (circuitSimulation != null)
    {
      Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
      for (ConnectionView nonTraceConnectionView : nonTraceConnectionViews)
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(circuitSimulation, nonTraceConnectionView);
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
      fireConnectionEvents(updatedConnectionViews, circuitSimulation);
    }
  }

  protected Set<ConnectionView> findNonTraceConnections(Set<TraceView> inputTraceViews)
  {
    Set<ConnectionView> nonTraceConnectionViews = new LinkedHashSet<>();
    for (TraceView inputTraceView : inputTraceViews)
    {
      List<ConnectionView> connectionViews = inputTraceView.getConnections();
      for (ConnectionView connectionView : connectionViews)
      {
        List<View> connectedComponents = connectionView.getConnectedComponents();
        for (View connectedComponent : connectedComponents)
        {
          if (!(connectedComponent instanceof TraceView))
          {
            nonTraceConnectionViews.add(connectionView);
          }
        }
      }
    }
    return nonTraceConnectionViews;
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

  public Set<ConnectionView> connectCreatedTraces(Set<TraceView> traceViews,
                                                  CircuitSimulation circuitSimulation)
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
    updatedConnectionViews = connectConnectionViews(connectionViews, circuitSimulation);
    return updatedConnectionViews;
  }

  public SubcircuitData save(Set<View> selection, long id)
  {
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

    return new SubcircuitData(componentDatas,
                              traceDatas,
                              typeName,
                              id);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews,
                                  CircuitSimulation simulation)
  {
    List<TraceView> connectedTraceViews = findImmediateConnectedTraceViews(staticViews);

    Set<TraceView> allConnectedTraceViews = new HashSet<>(connectedTraceViews);
    allConnectedTraceViews.removeAll(traceViews);

    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      connectionViews.addAll(disconnectStaticView(staticView, simulation));
    }

    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnections());
      disconnectTraceView(traceView);
    }

    connectConnectionViews(connectionViews, simulation);

    recreateTraceViews(new HashSet<>(), allConnectedTraceViews, simulation);
  }

  public List<View> doneMoveComponents(CircuitSimulation simulation,
                                       List<StaticView<?>> staticViews,
                                       List<TraceView> traceViews,
                                       Set<StaticView<?>> selectedViews)
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

    Set<ConnectionView> updatedConnectionViews = connectConnectionViews(connectionViews, simulation);
    enableStaticViews(staticViews, simulation);

    Set<TraceView> existingTraces = createTraceViews(existingLines, simulation);
    updatedConnectionViews.addAll(connectCreatedTraces(existingTraces, simulation));

    Set<TraceView> newTraces = createTraceViews(newLines, simulation);
    updatedConnectionViews.addAll(connectCreatedTraces(newTraces, simulation));

    fireConnectionEvents(updatedConnectionViews, simulation);

    return calculateNewSelection(staticViews, selectedViews, newTraces);
  }

  protected List<View> calculateNewSelection(List<StaticView<?>> staticViews,
                                             Set<StaticView<?>> selectedViews,
                                             Set<TraceView> newTraces)
  {
    List<View> newSelection = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      if (selectedViews.contains(staticView))
      {
        newSelection.add(staticView);
      }
    }
    newSelection.addAll(newTraces);

    return newSelection;
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

  public List<View> getSelectionFromRectangle(Float2D start,
                                              Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      updateSelectedViews(start,
                          end,
                          includeIntersections,
                          boundBoxPosition,
                          boundBoxDimension,
                          selectedViews,
                          staticView);
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

  public String getTypeName()
  {
    return typeName;
  }

  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }

  public List<PinView> findAllPins()
  {
    ArrayList<PinView> pinViews = new ArrayList<>();
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      if (passiveView instanceof PinView)
      {
        pinViews.add((PinView) passiveView);
      }
    }
    return pinViews;
  }

  public Set<SubcircuitInstanceView> getSubcircuitInstanceViews()
  {
    HashSet<SubcircuitInstanceView> result = new HashSet<>();
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      if (passiveView instanceof SubcircuitInstanceView)
      {
        result.add((SubcircuitInstanceView) passiveView);
      }
    }
    return result;
  }

  public void ensureComponentsForSimulation(TopLevelSubcircuitSimulation topLevelSubcircuitSimulation)
  {
    CircuitSimulation circuitSimulation = topLevelSubcircuitSimulation.getCircuitSimulation();
    for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
    {
      integratedCircuitView.simulationStarted(circuitSimulation);
    }
  }
}

