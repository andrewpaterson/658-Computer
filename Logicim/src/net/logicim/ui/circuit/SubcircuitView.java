package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.common.geometry.Line;
import net.common.geometry.LineMinimiser;
import net.common.geometry.LinePositionCache;
import net.common.geometry.LineSplitter;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Positions;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.*;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.LineOverlap;
import net.logicim.ui.common.TraceOverlap;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.*;
import net.logicim.ui.connection.LocalMultiSimulationConnectionNet;
import net.logicim.ui.connection.WireList;
import net.logicim.ui.connection.WireListFinder;
import net.logicim.ui.connection.WireTraceConverter;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.ConnectionViewCache;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.*;

public class SubcircuitView
{
  protected String typeName;  //The subcircuit instance name is on the SubcircuitInstanceView.

  protected CircuitEditor circuitEditor;

  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;
  protected Set<SubcircuitInstanceView> subcircuitInstanceViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected SubcircuitSimulations simulations;

  protected ConnectionViewCache connectionViewCache;

  public SubcircuitView(CircuitEditor circuitEditor)
  {
    this.circuitEditor = circuitEditor;

    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.subcircuitInstanceViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.connectionViewCache = new ConnectionViewCache();
    this.simulations = new SubcircuitSimulations();
  }

  public List<View> getAllViews()
  {
    List<View> views = new ArrayList<>();
    views.addAll(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    views.addAll(subcircuitInstanceViews);
    return views;
  }

  public List<StaticView<?>> getStaticViews()
  {
    List<StaticView<?>> views = new ArrayList<>();
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    views.addAll(subcircuitInstanceViews);
    return views;
  }

  public List<WireView> getWireViews()
  {
    List<WireView> views = new ArrayList<>();
    views.addAll(traceViews);
    views.addAll(tunnelViews);
    return views;
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews()
  {
    return new ArrayList<>(subcircuitInstanceViews);
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView instanceSubcircuitView)
  {
    ArrayList<SubcircuitInstanceView> result = new ArrayList<>();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      if (subcircuitInstanceView.getInstanceSubcircuitView() == instanceSubcircuitView)
      {
        result.add(subcircuitInstanceView);
      }
    }
    return result;
  }

  public List<ConnectionView> disconnectTraceViewAndDestroyComponents(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnectionViews();
    connectionViewCache.removeAll(traceView, connectionViews);
    traceView.disconnectViewAndDestroyComponents();
    return connectionViews;
  }

  public List<ConnectionView> disconnectStaticView(StaticView<?> staticView)
  {
    if (staticView == null)
    {
      throw new SimulatorException("Cannot disconnect [null] view.");
    }

    List<ConnectionView> connectionViews = staticView.getConnectionViews();
    if (connectionViews == null)
    {
      throw new SimulatorException("Cannot disconnect %s with [null] connections.", staticView.toIdentifierString());
    }

    connectionViewCache.removeAll(staticView, connectionViews);
    staticView.disconnectViewAndDestroyAllComponents();

    return connectionViews;
  }

  public void deleteStaticView(CircuitInstanceView circuitInstanceView, StaticView<?> staticView)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteStaticViews(circuitInstanceView, staticViews);
  }

  public void deleteStaticViews(CircuitInstanceView circuitInstanceView, List<StaticView<?>> staticViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("Cannot delete a [null] view.");
      }

      List<ConnectionView> disconnectedConnectionViews = disconnectStaticView(componentView);
      for (ConnectionView connectionView : disconnectedConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Disconnected connection may not be null from component [%s].", componentView.toIdentifierString());
        }
      }

      connectionViews.addAll(disconnectedConnectionViews);
      if (componentView instanceof IntegratedCircuitView)
      {
        removeIntegratedCircuitView((IntegratedCircuitView<?, ?>) componentView);
      }
      else if (componentView instanceof PassiveView)
      {
        removePassiveView((PassiveView<?, ?>) componentView);
      }
      else if (componentView instanceof SubcircuitInstanceView)
      {
        removeSubcircuitInstanceView((SubcircuitInstanceView) componentView);
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

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(circuitInstanceView, connectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public void fireConnectionEvents(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = getPortViews(connectionViews);
    for (PortView portView : portViews)
    {
      portView.traceConnected();
    }
  }

  public void deleteTraceViews(CircuitInstanceView circuitInstanceView, Set<TraceView> inputTraceViews)
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

    traceFinder.process();
    createTraceViews(circuitInstanceView, new HashSet<>(), traceFinder.getTraceViews());

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(circuitInstanceView, nonTraceConnectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public ConnectionView getOrAddConnectionView(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnectionView(position, view);
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews,
                                  integratedCircuitViews,
                                  passiveViews,
                                  subcircuitInstanceViews,
                                  decorativeViews);
  }

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public void validate()
  {
    validateConnectionViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();

    validateComponentSimulations();
  }

  protected void validateComponentSimulations()
  {
    validateComponentSimulations(subcircuitInstanceViews, "subcircuit instance view");
    validateComponentSimulations(integratedCircuitViews, "integrated circuit view");
    validateComponentSimulations(passiveViews, "passive view");

    validateWireSimulations(traceViews, "trace view");
    validateWireSimulations(tunnelViews, "tunnel view");
  }

  protected void validateComponentSimulations(Set<? extends ComponentView<?>> componentViews, String type)
  {
    for (ComponentView<?> componentView : componentViews)
    {
      if (componentView.isEnabled())
      {
        Set<SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
        Set<SubcircuitSimulation> componentSubcircuitSimulations = componentView.getComponentSubcircuitSimulations();
        if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
        {
          throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                       getTypeName(),
                                       simulationSubcircuitSimulations.size(),
                                       type,
                                       componentView.getDescription(),
                                       componentSubcircuitSimulations);
        }

        validatePortSimulations(componentView.getPortViews());
      }
    }
  }

  protected void validatePortSimulations(List<PortView> portViews)
  {
    for (PortView portView : portViews)
    {
      Set<SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
      Set<SubcircuitSimulation> componentSubcircuitSimulations = portView.getPortSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all port view [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     portView.getDescription(),
                                     componentSubcircuitSimulations);
      }
    }
  }

  protected void validateWireSimulations(Set<? extends WireView> wireViews, String type)
  {
    for (WireView wireView : wireViews)
    {
      Set<SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
      Set<SubcircuitSimulation> componentSubcircuitSimulations = wireView.getWireSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     type,
                                     wireView.getDescription(),
                                     componentSubcircuitSimulations);
      }
    }
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

      List<ConnectionView> localConnectionViews = staticView.getConnectionViews();
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
          else if (view instanceof SubcircuitInstanceView)
          {
            if (!subcircuitInstanceViews.contains(view))
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

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  public void addSubcircuitInstanceView(SubcircuitInstanceView view)
  {
    synchronized (this)
    {
      subcircuitInstanceViews.add(view);
    }
  }

  public void addDecorativeView(DecorativeView<?> view)
  {
    synchronized (this)
    {
      decorativeViews.add(view);
    }
  }

  protected void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  protected boolean removeSubcircuitInstanceView(SubcircuitInstanceView subcircuitInstanceView)
  {
    synchronized (this)
    {
      return subcircuitInstanceViews.remove(subcircuitInstanceView);
    }
  }

  protected boolean removeDecorativeView(DecorativeView<?> decorativeView)
  {
    synchronized (this)
    {
      return decorativeViews.remove(decorativeView);
    }
  }

  protected void removeTraceView(TraceView traceView)
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

  public void addTraceView(TraceView view)
  {
    synchronized (this)
    {
      traceViews.add(view);
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

  public Set<ConnectionView> createTracesForConnectionViews(CircuitInstanceView circuitInstanceView,
                                                            Collection<ConnectionView> tracesConnectionViews)
  {
    CircuitInstanceViewPaths paths = getCircuitEditor().createCircuitInstanceViewPaths();

    Set<ConnectionView> allUpdatedConnectionViews = new LinkedHashSet<>();
    if (tracesConnectionViews.size() > 0)
    {
      validateConnectionViewsNotNull(tracesConnectionViews);
      Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViewsForSubcircuitSimulation(circuitInstanceView,
                                                                                                         tracesConnectionViews,
                                                                                                         paths);
      allUpdatedConnectionViews.addAll(updatedConnectionViews);
    }

    // allUpdatedConnectionViews are returned for fireConnectionEvents.  Simulations that were already connected (is that possible?) should not fireConnectionEvents.  You should validate this on firing.
    return allUpdatedConnectionViews;
  }

  protected void validateConnectionViewsNotNull(Collection<ConnectionView> tracesConnectionViews)
  {
    for (ConnectionView connectionView : tracesConnectionViews)
    {
      if (connectionView == null)
      {
        throw new SimulatorException("Connection View may not be null.");
      }
    }
  }

  protected Set<ConnectionView> createTracesForConnectionViewsForSubcircuitSimulation(CircuitInstanceView circuitInstanceView,
                                                                                      Collection<ConnectionView> tracesConnectionViews,
                                                                                      CircuitInstanceViewPaths paths)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : tracesConnectionViews)
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        WireList wireList = WireListFinder.findTraceConnections(circuitInstanceView, connectionView, paths);

        for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
        {
          WireTraceConverter wireTraceConverter = new WireTraceConverter(wireList, subcircuitSimulation, paths);
          wireTraceConverter.process();
        }

        List<LocalMultiSimulationConnectionNet> connectionNets = wireList.getConnectionNets();
        List<ConnectionView> connectionNetConnectionViews = WireListFinder.getConnectionViews(connectionNets);
        updatedConnectionViews.addAll(connectionNetConnectionViews);
      }
    }
    return updatedConnectionViews;
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    for (TraceView traceView : traceViews)
    {
      traceView.disable();
      disconnectTraceViewAndDestroyComponents(traceView);
    }

    for (TraceView traceView : traceViews)
    {
      removeTraceView(traceView);
    }
  }

  public List<ConnectionView> getOrCreateStaticViewConnections(List<StaticView<?>> staticViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getOrCreateConnectionViews(this));
    }
    return connectionViews;
  }

  public List<ConnectionView> getConnectionViews(List<StaticView<?>> staticViews,
                                                 Set<TraceView> traceViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getConnectionViews());
    }
    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnectionViews());
    }
    return connectionViews;
  }

  public void enableStaticViews(List<StaticView<?>> staticViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.enable();
    }
  }

  private void createComponentsForAllSimulations(List<StaticView<?>> staticViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.createComponents(simulations);
    }
  }

  public void simulationStarted(List<StaticView<?>> staticViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.simulationStarted();
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
          PortView portView = componentView.getPortView(connectionView);
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

  public Set<TraceView> createTraceViews(CircuitInstanceView circuitInstanceView, Collection<Line> traceLineViews)
  {
    TraceFinder traceFinder = new TraceFinder();
    for (Line line : traceLineViews)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(line);
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        traceFinder.add(traceOverlap.getTraceView());
      }
    }

    traceFinder.process();

    return createTraceViews(circuitInstanceView, traceLineViews, traceFinder.getTraceViews());
  }

  protected Set<TraceView> createTraceViews(CircuitInstanceView circuitInstanceView,
                                            Collection<Line> newTraceViewLines,
                                            Collection<TraceView> touchingTraceViews)
  {
    Set<Line> traceLines = new LinkedHashSet<>(newTraceViewLines);
    traceLines.addAll(getTraceViewLines(touchingTraceViews));
    removeTraceViews(touchingTraceViews);
    Set<TraceView> traceViews = generateNewTraces(traceLines);

    Set<ConnectionView> tracesConnectionViews = getTracesConnectionViews(traceViews);
    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(circuitInstanceView, tracesConnectionViews);
    fireConnectionEvents(updatedConnectionViews);
    return traceViews;
  }

  public static List<Line> getTraceViewLines(Collection<TraceView> traceViews)
  {
    List<Line> newLines = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      newLines.add(traceView.getLine());
    }
    return newLines;
  }

  protected Set<ConnectionView> findNonTraceConnections(Set<TraceView> inputTraceViews)
  {
    Set<ConnectionView> nonTraceConnectionViews = new LinkedHashSet<>();
    for (TraceView inputTraceView : inputTraceViews)
    {
      List<ConnectionView> connectionViews = inputTraceView.getConnectionViews();
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

  private Set<TraceView> generateNewTraces(Collection<Line> lines)
  {
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Positions positionMap = new Positions(lines);

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    StaticViewIterator iterator = staticViewIterator();

    List<Int2D> additionalJunctions = new ArrayList<>();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      List<ConnectionView> connections = staticView.getConnectionViews();
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

  protected Set<ConnectionView> getTracesConnectionViews(Set<TraceView> traceViews)
  {
    Set<ConnectionView> connectionViews = new HashSet<>();
    int i = 0;
    for (TraceView traceView : traceViews)
    {
      if (!traceView.hasConnections())
      {
        throw new SimulatorException("Cannot get a connection for a removed Trace.  Iteration [%s].", i);
      }
      connectionViews.add(traceView.getStartConnection());
      connectionViews.add(traceView.getEndConnection());
      i++;
    }
    return connectionViews;
  }

  public SubcircuitEditorData save(Set<View> selection, long id)
  {
    ArrayList<StaticData<?>> staticDatas = new ArrayList<>();
    ArrayList<SubcircuitInstanceData> subcircuitInstanceDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }
      else if (staticData instanceof SubcircuitInstanceData)
      {
        subcircuitInstanceDatas.add((SubcircuitInstanceData) staticData);
      }
      else
      {
        staticDatas.add(staticData);
      }
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    SubcircuitData subcircuitData = new SubcircuitData(staticDatas, subcircuitInstanceDatas, traceDatas);
    return new SubcircuitEditorData(subcircuitData, typeName, id);
  }

  public void startMoveComponents(CircuitInstanceView circuitInstanceView,
                                  List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews)
  {
    List<TraceView> connectedTraceViews = findImmediateConnectedTraceViews(staticViews);
    Set<TraceView> connectedOtherTraceViews = new LinkedHashSet<>(connectedTraceViews);
    connectedOtherTraceViews.removeAll(traceViews);

    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      updatedConnectionViews.addAll(disconnectStaticView(staticView));
    }

    for (TraceView traceView : traceViews)
    {
      traceView.disable();
      updatedConnectionViews.addAll(disconnectTraceViewAndDestroyComponents(traceView));
    }

    createTraceViews(circuitInstanceView, new HashSet<>(), connectedOtherTraceViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public List<View> doneMoveComponents(CircuitInstanceView circuitInstanceView,
                                       List<StaticView<?>> componentViews,
                                       List<Line> newTraceViewLines,
                                       List<TraceView> removeTraceViews,
                                       Set<StaticView<?>> selectedViews)
  {
    removeTraceViews(new LinkedHashSet<>(removeTraceViews));

    getOrCreateStaticViewConnections(componentViews);
    Set<TraceView> existingTraceViews = getComponentConnectionTraceViews(componentViews);

    List<Line> existingLines = getTraceViewLines(existingTraceViews);
    removeTraceViews(existingTraceViews);

    enableStaticViews(componentViews);

    createComponentsForAllSimulations(componentViews);

    createTraceViews(circuitInstanceView, existingLines);
    Set<TraceView> newTraces = createTraceViews(circuitInstanceView, newTraceViewLines);

    simulationStarted(componentViews);

    return calculateNewSelection(componentViews, selectedViews, newTraces);
  }

  protected Set<TraceView> getComponentConnectionTraceViews(List<StaticView<?>> componentViews)
  {
    Set<ConnectionView> junctions = getComponentConnectionPositions(componentViews);
    Set<TraceView> existingTraceViews = new LinkedHashSet<>();
    for (ConnectionView junction : junctions)
    {
      Int2D position = junction.getGridPosition();
      existingTraceViews.addAll(getTraceViewsInGridSpace(position.x, position.y));
    }
    return existingTraceViews;
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
      List<ConnectionView> connectionViews = staticView.getConnectionViews();
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
      List<ConnectionView> connectionViews = staticView.getConnectionViews();
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

  @Override
  public String toString()
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

  public Set<SubcircuitInstanceView> findAllSubcircuitInstanceViews()
  {
    return subcircuitInstanceViews;
  }

  public Set<StaticView<?>> findAllViewsOfClass(Class<? extends StaticView<?>> viewClass)
  {
    Set<StaticView<?>> views = new LinkedHashSet<>();
    if (DecorativeView.class.isAssignableFrom(viewClass))
    {
      for (DecorativeView<?> decorativeView : decorativeViews)
      {
        if (viewClass.isAssignableFrom(decorativeView.getClass()))
        {
          views.add(decorativeView);
        }
      }
    }
    else if (IntegratedCircuitView.class.isAssignableFrom(viewClass))
    {
      for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
      {
        if (viewClass.isAssignableFrom(integratedCircuitView.getClass()))
        {
          views.add(integratedCircuitView);
        }
      }
    }
    else if (PassiveView.class.isAssignableFrom(viewClass))
    {
      for (PassiveView<?, ?> passiveView : passiveViews)
      {
        if (viewClass.isAssignableFrom(passiveView.getClass()))
        {
          views.add(passiveView);
        }
      }
    }
    else if (SubcircuitInstanceView.class.isAssignableFrom(viewClass))
    {
      for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
      {
        if (viewClass.isAssignableFrom(subcircuitInstanceView.getClass()))
        {
          views.add(subcircuitInstanceView);
        }
      }
    }
    return views;
  }

  public List<String> getSubcircuitInstanceNames()
  {
    Set<String> names = new HashSet<>();
    Set<SubcircuitInstanceView> subcircuitInstanceViews = findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      names.add(subcircuitInstanceView.getTypeName());
    }
    return new ArrayList<>(names);
  }

  public List<String> getTypeNameAsList()
  {
    ArrayList<String> list = new ArrayList<>();
    list.add(getTypeName());
    return list;
  }

  public void destroySubcircuitInstanceComponentsAndSimulations(SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    destroyComponents(getStaticViews(), traceViews, subcircuitInstanceSimulation);

    simulations.remove(subcircuitInstanceSimulation);
  }

  protected void destroyComponents(Collection<StaticView<?>> staticViews,
                                   Collection<TraceView> traceViews,
                                   SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    for (StaticView<?> staticView : staticViews)
    {
      if (!(staticView instanceof SubcircuitInstanceView))
      {
        staticView.destroyComponent(subcircuitInstanceSimulation);
      }
    }

    for (TraceView traceView : traceViews)
    {
      traceView.disconnectViewAndDestroyComponents();
    }

    for (StaticView<?> staticView : staticViews)
    {
      if (staticView instanceof SubcircuitInstanceView)
      {
        staticView.destroyComponent(subcircuitInstanceSimulation);
      }
    }
  }

  public void createComponentsForSubcircuitInstanceView(SubcircuitInstanceSimulation subcircuitInstanceSimulation)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.createComponent(subcircuitInstanceSimulation);
    }

    simulationStarted(staticViews);
  }

  public void createTracesForSubcircuitInstanceView(CircuitInstanceView circuitInstanceView)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    List<ConnectionView> connectionViews = getConnectionViews(staticViews, traceViews);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(circuitInstanceView, connectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public SubcircuitTopSimulation createSubcircuitTopSimulation(String name)
  {
    SubcircuitTopSimulation subcircuitTopSimulation = new SubcircuitTopSimulation(new CircuitSimulation(name));
    addSubcircuitSimulation(subcircuitTopSimulation);
    return subcircuitTopSimulation;
  }

  public List<SubcircuitSimulation> getSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    return simulations.getSubcircuitSimulations(circuitSimulation);
  }

  public SubcircuitSimulations getSimulations()
  {
    return simulations;
  }

  public List<SubcircuitTopSimulation> getTopSimulations()
  {
    List<SubcircuitTopSimulation> list = new ArrayList<>();
    for (SubcircuitTopSimulation subcircuitSimulation : simulations.getSubcircuitTopSimulations())
    {
      list.add(subcircuitSimulation);
    }
    return list;
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    simulations.add(subcircuitSimulation);
  }

  public void validateSimulations(List<CircuitInstanceView> orderedTopDownCircuitInstanceViews)
  {
    Collection<SubcircuitTopSimulation> subcircuitTopSimulations = simulations.getSubcircuitTopSimulations();
    if ((subcircuitTopSimulations.isEmpty()))
    {
      throw new SimulatorException("Expected at least one subcircuit top simulation.");
    }

    if (orderedTopDownCircuitInstanceViews.size() == 0)
    {
      throw new SimulatorException("Expected at least one circuit instance view.");
    }
    CircuitInstanceView circuitInstanceView = orderedTopDownCircuitInstanceViews.get(0);
    if (circuitInstanceView.getCircuitSubcircuitView() != this)
    {
      throw new SimulatorException("First circuit instance view");
    }

    simulations.validate(orderedTopDownCircuitInstanceViews);
  }

  public SubcircuitTopSimulation addNewSimulation(String simulationName)
  {
    List<SubcircuitTopSimulation> existingTopSimulations = getTopSimulations();
    SubcircuitTopSimulation startTopSimulation = existingTopSimulations.get(0);

    TraceToTraceMap traceMap = new TraceToTraceMap();
    SubcircuitTopSimulation newSubcircuitTopSimulation = createSubcircuitTopSimulation(simulationName);
    recurseAddNewSimulation(startTopSimulation, newSubcircuitTopSimulation, traceMap);

    return newSubcircuitTopSimulation;
  }

  protected void recurseAddNewSimulation(SubcircuitSimulation existingSimulation,
                                         SubcircuitSimulation newSimulation,
                                         TraceToTraceMap traceMap)
  {
    addSimulationToWires(existingSimulation, newSimulation, traceMap);
    addSimulationToPassives(existingSimulation, newSimulation, traceMap);
    addSimulationToIntegratedCircuits(existingSimulation, newSimulation, traceMap);
    addSimulationToSubcircuitInstances(existingSimulation, newSimulation, traceMap);
  }

  private void addSimulationToSubcircuitInstances(SubcircuitSimulation existingSimulation,
                                                  SubcircuitSimulation newSimulation,
                                                  TraceToTraceMap traceMap)
  {
    CircuitSimulation existingCircuitSimulation = existingSimulation.getCircuitSimulation();
    List<SubcircuitInstanceView> subcircuitInstanceViews = getSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      List<SubcircuitInstanceSimulation> innerSubcircuitSimulations = subcircuitInstanceView.getInnerSubcircuitSimulations(existingCircuitSimulation);
      for (SubcircuitInstanceSimulation innerSubcircuitSimulation : innerSubcircuitSimulations)
      {
        SubcircuitInstance subcircuitInstance = subcircuitInstanceView.createComponent(newSimulation);
        newSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();

        SubcircuitView instanceSubcircuitView = subcircuitInstanceView.getInstanceSubcircuitView();

        instanceSubcircuitView.recurseAddNewSimulation(innerSubcircuitSimulation, newSimulation, traceMap);
      }
    }
  }

  private void addSimulationToIntegratedCircuits(SubcircuitSimulation existingSimulation,
                                                 SubcircuitSimulation newSimulation,
                                                 TraceToTraceMap traceMap)
  {
    for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
    {
      IntegratedCircuit<?, ?> existingIntegratedCircuit = integratedCircuitView.getComponent(existingSimulation);
      IntegratedCircuit<?, ?> newIntegratedCircuit = integratedCircuitView.createComponent(newSimulation);
      connectPorts(existingIntegratedCircuit.getPorts(), newIntegratedCircuit.getPorts(), traceMap);
      newIntegratedCircuit.simulationStarted(newSimulation.getSimulation());
    }
  }

  private void addSimulationToPassives(SubcircuitSimulation existingSimulation,
                                       SubcircuitSimulation newSimulation,
                                       TraceToTraceMap traceMap)
  {
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      Passive existingPassive = passiveView.getComponent(existingSimulation);
      Passive newPassive = passiveView.createComponent(newSimulation);
      connectPorts(existingPassive.getPorts(), newPassive.getPorts(), traceMap);
      newPassive.simulationStarted(newSimulation.getSimulation());
    }
  }

  private void addSimulationToWires(SubcircuitSimulation existingSimulation,
                                    SubcircuitSimulation newSimulation,
                                    TraceToTraceMap traceMap)
  {
    List<WireView> wireViews = getWireViews();
    for (WireView wireView : wireViews)
    {
      WireViewComp wireViewComp = wireView.getWireViewComp();
      List<Trace> existingTraces = wireViewComp.getTraces(existingSimulation);
      List<Trace> newTraces = traceMap.makeTraces(existingTraces);
      wireViewComp.connectTraces(newSimulation, newTraces);
    }
  }

  private void connectPorts(List<Port> existingPorts, List<Port> newPorts, TraceToTraceMap traceMap)
  {
    for (int i = 0; i < newPorts.size(); i++)
    {
      Port existingPort = existingPorts.get(i);
      if (existingPort.isExplicit())
      {
        Port newPort = newPorts.get(i);
        Trace existingTrace = existingPort.getTrace();
        Trace newTrace = traceMap.makeTrace(existingTrace);
        newPort.connect(newTrace);
      }
    }
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }
}

