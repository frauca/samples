export interface Riddle {
    name: string;
    max: number;
    min: number;
    guess: number;
    result?: string;
}

export interface Answer {
    guess: number;
}
