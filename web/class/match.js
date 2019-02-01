class Match {
	constructor (id, joueur1, joueur2, court, tournament, stratTime, points, duration, serverPlayer, speed_last_serv, nb_exchanges, contests, final) {
    this.id = id;
    this.joueur1 = joueur1;
    this.joueur2 = joueur2;
    this.court = court;
    this.tournament = tournament;
    this.stratTime = stratTime;
    this.points = points;
    this.duration = duration;
    this.serverPlayer = serverPlayer;
    this.speed_last_serv = speed_last_serv;
    this.nb_exchanges = 0;
    this.contests = contests;
    this.final = final;
  }

  	score (points) {
		switch (points){
            case 0 :
                return 0;
            case 1 :
                return 15;
            case 2 :
                return 30;
            case 3 :
                return 40;
            default :
                return 0;
        }
	}

	time (t){
		var hours = Math.floor(t / 3600);
        var minutes = Math.floor((t % 3600) / 60);
        var seconds = t - minutes * 60 - hours * 3600;

        return hours + "h " + minutes + "min " + seconds + "s";
	}

    isFinal() {
        return this.final;
    }

    setEndProcessed() {
        console.log("setEndProcessed function");
        console.log("data in cache: ");

        let processed = localStorage.getItem(this.id + "_end");
        console.log(processed);

        if(processed === null) {
            localStorage.setItem(this.id + "_end", true);
            console.log("data just saved: ");
            console.log(localStorage.getItem(this.id + "_end"));
        }
    }

    isEndProcessed() {
        console.log("isEndProcessed function");
        console.log("From cache, is already processed: ");
        let processed = localStorage.getItem(this.id + "_end");
        console.log(processed);

        if(processed === null) {
            return false;
        } else {
            return true;
        }
    } 
}







