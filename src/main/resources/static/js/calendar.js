
  document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
      headerToolbar: {
        left: 'prevYear,prev,next,nextYear today',
        center: 'title',
        right: 'dayGridMonth,dayGridWeek,dayGridDay'
      },
      locale: 'ko',
      navLinks: true,
      editable: true,
//      events: 'http://localhost:8080/event_get_all',

      eventDrop: function(info) {
        alert(info.event.start);
        let data = {
          'id': info.event.id,
          'title': info.event.title,
          'start': info.event.start,
          'end': info.event.end
        };

        const response = fetch('http://localhost:8080/event_drag_and_drop', {
          method: 'POST',
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        })
      },

      selectable: true,
      select: function (info) {
        const eventName = prompt("일정을 입력하십시오.");
        if (eventName) {
          let data = {
            'title': eventName,
            'start': info.start,
            'end': info.end
          };

          const response = fetch('http://localhost:8080/event_add', {
            method: 'POST',
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
          })
          .then(response => response.json())
          .then(data => {
            if(data.result === true) {
              calendar.addEvent({
                title: eventName,
                start: info.start,
                end: info.end,
                allDay: true,
              });
            }
          });
        }
      },

      eventClick: function(info) {
        alert(info.event.id);

        let deleteGuide  = `「${info.event.title}」 일정을 삭제하십니까?`;
        if (confirm(deleteGuide)) {
          info.event.remove();
          let data = {
            id: info.event.id,
          };
          const response = fetch('http://localhost:8080/event_remove', {
            method: 'POST',
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
          });
        }
      },

      // timeZone: 'Asia/Seoul',
      events: "http://localhost:8080/event_get_all"
    });
    //calendar.rerenderEvents();
    calendar.render();
  });

  //setInterval(function(){document.getElementById('calendar').fullCalendar('refetchEvents')}, 1000);
  // const response = fetch('http://localhost:8080/event_add', {
  //   method: 'POST',
  //   headers: {
  //     "Content-Type": "application/json",
  //   },
  //   body: JSON.stringify(data),
  // });
