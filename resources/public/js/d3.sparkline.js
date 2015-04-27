
var width = 100;
var height = 50;
var x = d3.scale.linear().range([width, 0]);
var y = d3.scale.linear().range([height, 0]);
var line = d3.svg.line()
                 .x(function(d, i) { return i*2; console.log(i); })
                 .y(function(d, i) { return x(d); console.log(d) })

  function sparkline(data) {

     x.domain([0, d3.max(data)])

    document.getElementById("points-sparkline").innerHTML = '';
    d3.select("#points-sparkline")
      .style("background-color", "white")
      .append('svg')
      .attr('width', width)
      .attr('height', height)
      .append('path')
      .attr('class', 'sparkline')
      .data([data])
      .attr('d', line);

  }

